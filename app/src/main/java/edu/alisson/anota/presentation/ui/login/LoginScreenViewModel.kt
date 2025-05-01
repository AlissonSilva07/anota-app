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

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _authState = MutableStateFlow<Resource<FirebaseUser?>>(Resource.Success(null))
    val authState = _authState.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login() = viewModelScope.launch {
        val email = _email.value.trim()
        val password = _password.value

        if (email.isBlank()) {
            _eventFlow.emit(UiEvent.ShowToast("E-mail não pode estar vazio."))
            return@launch
        }

        if (password.length < 6) {
            _eventFlow.emit(UiEvent.ShowToast("Senha deve ter pelo menos 6 caracteres."))
            return@launch
        }

        _authState.value = Resource.Loading()
        val result = authRepository.login(email, password)
        _authState.value = result

        if (result is Resource.Error) {
            _eventFlow.emit(UiEvent.ShowToast(result.message ?: "Erro ao fazer login."))
        } else if (result is Resource.Success) {
            _eventFlow.emit(UiEvent.NavigateToHome)
        }
    }
}