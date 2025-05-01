package edu.alisson.anota.presentation.ui.login

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    object NavigateToHome : UiEvent()
}