package edu.alisson.anota.presentation.ui.note

sealed class NoteIntent {
    data object Create : NoteIntent()
    data class Edit(val spaceId: String, val noteId: String) : NoteIntent()
}