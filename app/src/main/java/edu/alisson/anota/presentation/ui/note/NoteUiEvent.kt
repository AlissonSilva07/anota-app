package edu.alisson.anota.presentation.ui.note

sealed class NoteUiEvent {
    data class ShowToast(val message: String) : NoteUiEvent()
}