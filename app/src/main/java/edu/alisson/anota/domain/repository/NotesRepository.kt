package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note

interface NotesRepository {
    suspend fun saveNote(note: Note): Resource<Nothing>
}