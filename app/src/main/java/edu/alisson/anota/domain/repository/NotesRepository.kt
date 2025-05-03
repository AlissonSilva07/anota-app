package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun saveNote(note: Note): Resource<Nothing>
    suspend fun getNoteById(spaceId: String, noteId: String): Resource<Note>
    suspend fun editNoteById(spaceId: String, noteId: String, updatedNote: Note): Resource<Nothing>
    suspend fun deleteNoteById(spaceId: String, noteId: String): Resource<Nothing>
    suspend fun getLastSeenNote(): Note?
    suspend fun saveLastSeenNoteLocally(note: Note)
    suspend fun deleteLastSeenNoteById()
}