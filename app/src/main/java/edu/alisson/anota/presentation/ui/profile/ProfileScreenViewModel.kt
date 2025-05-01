package edu.alisson.anota.presentation.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val user = authRepository.getCurrentUser()
        user?.let {
            _userData.value = User(
                uid = it.uid,
                name = it.displayName ?: "Nome",
                email = it.email ?: "Email",
                photoUrl = it.photoUrl?.toString()
            )
        }
    }

    fun logout() {
        authRepository.logout()
    }
}