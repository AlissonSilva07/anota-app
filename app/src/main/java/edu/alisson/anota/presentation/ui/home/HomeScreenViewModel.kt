package edu.alisson.anota.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.DataStoreManager
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    private val _userDataResponse = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userDataResponse = _userDataResponse.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.uidFlow.collect { uid ->
                if (!uid.isNullOrBlank()) {
                    fetchUserData(uid)
                }
            }
        }
    }

    private suspend fun fetchUserData(uid: String) {
        try {
            val result = userRepository.getUserData(uid)

            when (result) {
                is Resource.Success -> {
                    _userData.value = result.data
                    _userDataResponse.value = Resource.Success(result.data)
                    Log.d("HomeScreenViewModel", "User data: ${result.data}")
                }
                is Resource.Error<*> -> {
                    _userDataResponse.value = Resource.Error("Error fetching user data")
                    Log.e("HomeScreenViewModel", "Error: ${result.message}")
                }
                is Resource.Loading<*> -> {
                    Log.d("HomeScreenViewModel", "Loading user data")
                }
            }
        } catch (e: Exception) {
            _userDataResponse.value = Resource.Error(e.message ?: "An error occurred")
            Log.e("HomeScreenViewModel", "Exception: ${e.message}")
        }
    }
}

