package edu.alisson.anota.presentation.ui.login

sealed class LoginUiEvent {
    data class ShowToast(val message: String) : LoginUiEvent()
    object NavigateToHome : LoginUiEvent()
}