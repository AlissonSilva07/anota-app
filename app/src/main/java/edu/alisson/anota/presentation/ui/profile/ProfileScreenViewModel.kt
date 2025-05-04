package edu.alisson.anota.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.DataStoreManager
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.AuthRepository
import edu.alisson.anota.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    private val _userDataResponse = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userDataResponse = _userDataResponse.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.uidFlow.collect { uid ->
                if (uid != null) {
                    getUserData(uid)
                }
            }
        }
    }

    private suspend fun getUserData(uid: String) {
        val localUserData = userRepository.getLocalUser(uid)
        if (localUserData != null) {
            _userData.value = localUserData
            _userDataResponse.value = Resource.Success(localUserData)
        } else {
            fetchUserDataFromRemote(uid)
        }
    }

    private suspend fun fetchUserDataFromRemote(uid: String) {
        try {
            _userDataResponse.value = Resource.Loading()
            val result = userRepository.getUserData(uid)

            when (result) {
                is Resource.Success -> {
                    _userData.value = result.data
                    _userDataResponse.value = Resource.Success(result.data)
                    Log.d("ProfileScreenViewModel", "User data fetched: ${result.data}")
                }

                is Resource.Error<*> -> {
                    _userDataResponse.value = Resource.Error("Error fetching user data")
                    Log.e("ProfileScreenViewModel", "Error: ${result.message}")
                }

                is Resource.Loading<*> -> {
                    Log.d("ProfileScreenViewModel", "Loading user data")
                }
            }
        } catch (e: Exception) {
            _userDataResponse.value = Resource.Error(e.message ?: "An error occurred")
            Log.e("ProfileScreenViewModel", "Exception: ${e.message}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authRepository.logout()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("ProfileScreenViewModel", "Exception: ${e.message}")
            }
        }
    }
}
