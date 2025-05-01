package edu.alisson.anota.presentation.ui.signup

sealed class SignUpUiEvent {
    data class ShowToast(val message: String) : SignUpUiEvent()
    object NavigateToLogin : SignUpUiEvent()
}