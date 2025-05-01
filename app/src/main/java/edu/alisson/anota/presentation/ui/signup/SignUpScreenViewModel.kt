package edu.alisson.anota.presentation.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.repository.AuthRepository
import edu.alisson.anota.presentation.ui.signup.SignUpUiEvent.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordRepeat = MutableStateFlow("")
    val passwordRepeat = _passwordRepeat.asStateFlow()

    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    // Error states
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _passwordRepeatError = MutableStateFlow<String?>(null)
    val passwordRepeatError = _passwordRepeatError.asStateFlow()

    // UI Events
    private val _eventFlow = MutableSharedFlow<SignUpUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onPasswordRepeatChange(value: String) { _passwordRepeat.value = value }
    fun onProfileImageChange(base64: String?) { _profileImageUri.value = base64 }

    fun signUp() = viewModelScope.launch {
        // Reset errors
        _nameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _passwordRepeatError.value = null

        val name = _name.value
        val email = _email.value.trim()
        val password = _password.value
        val passwordRepeat = _passwordRepeat.value
        val profileImageUri = _profileImageUri.value

        var hasError = false

        if (name.isEmpty()) {
            _nameError.value = "Nome é obrigatório"
            hasError = true
        }

        if (email.isEmpty()) {
            _emailError.value = "Email é obrigatório"
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Email inválido"
            hasError = true
        }

        if (password.length < 6) {
            _passwordError.value = "Senha deve ter no mínimo 6 caracteres"
            hasError = true
        }

        if (passwordRepeat != password) {
            _passwordRepeatError.value = "As senhas não coincidem"
            hasError = true
        }

        if (hasError) return@launch

        val result = authRepository.signUpWithImage(
            name = name,
            email = email,
            password = password,
            base64Image = profileImageUri
        )


        when (result) {
            is Resource.Success -> _eventFlow.emit(SignUpUiEvent.NavigateToLogin)
            is Resource.Error -> _eventFlow.emit(ShowToast(result.message ?: "Erro ao cadastrar."))
            is Resource.Loading<*> -> TODO()
        }
    }
}