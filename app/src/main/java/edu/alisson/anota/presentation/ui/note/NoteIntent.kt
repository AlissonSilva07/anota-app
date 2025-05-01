package edu.alisson.anota.presentation.ui.note

sealed class NoteIntent {
    data object Create : NoteIntent()
    data class Edit(val noteId: String) : NoteIntent()
}