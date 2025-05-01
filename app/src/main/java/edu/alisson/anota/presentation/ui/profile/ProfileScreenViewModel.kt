package edu.alisson.anota.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.data.utils.Resource.*
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    private val _userDataResponse = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userDataResponse = _userDataResponse.asStateFlow()

    init {
        fetchUserData()
    }

    fun fetchUserData() = viewModelScope.launch {
        val currentUser = authRepository.getCurrentUser()
        val uid = currentUser?.uid ?: ""

        try {
            _userDataResponse.value = Resource.Loading()
            val result = authRepository.getUserData(uid)

            when (result) {
                is Resource.Success -> {
                    _userData.value = result.data
                    Log.d("ProfileScreenViewModel", "User data fetched successfully: ${result.data}")

                    _userDataResponse.value = Success(result.data)
                    Log.d("ProfileScreenViewModel", "User data fetched successfully: ${result.data}")
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
        authRepository.logout()
    }
}