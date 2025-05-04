package edu.alisson.anota.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _authState = MutableStateFlow<Resource<FirebaseUser?>>(Resource.Success(null))
    val authState = _authState.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    private val _eventFlow = MutableSharedFlow<LoginUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login() = viewModelScope.launch {
        _isLoading.value = true
        val email = _email.value.trim()
        val password = _password.value

        _emailError.value = null
        _passwordError.value = null

        var hasError = false

        if (email.isBlank()) {
            _isLoading.value = false
            _emailError.value = "E-mail não pode estar vazio."
            hasError = true
        }

        if (password.length < 6) {
            _isLoading.value = false
            _passwordError.value = "Senha deve ter pelo menos 6 caracteres."
            hasError = true
        }

        if (hasError) {
            _isLoading.value = false
            return@launch
        }

        _authState.value = Resource.Loading()
        val result = authRepository.login(email, password)
        _authState.value = result

        if (result is Resource.Error) {
            _isLoading.value = false
            _eventFlow.emit(LoginUiEvent.ShowToast(result.message ?: "Erro ao fazer login."))
        } else if (result is Resource.Success) {
            _isLoading.value = false
            _eventFlow.emit(LoginUiEvent.NavigateToHome)
        }
    }
}