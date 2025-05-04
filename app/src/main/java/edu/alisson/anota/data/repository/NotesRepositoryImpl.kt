package edu.alisson.anota.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.local.last_note.LastSeenNoteDao
import edu.alisson.anota.data.local.last_note.LastSeenNoteEntity
import edu.alisson.anota.data.mappers.toNote
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.repository.NotesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class NotesRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val lastSeenNoteDao: LastSeenNoteDao
): NotesRepository {

    override suspend fun saveNote(note: Note): Resource<Nothing> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val spaceRef = database.reference
                .child("users")
                .child(uid)
                .child("spaces")
                .child(note.spaceID)

            val spaceSnapshot = spaceRef.get().await()
            if (!spaceSnapshot.exists()) {
                return Resource.Error("Espaço não encontrado.")
            }

            val spaceTitle = spaceSnapshot.child("title").getValue(String::class.java)
                ?: return Resource.Error("Título do espaço não encontrado.")

            val newNoteRef = spaceRef.child("notes").push()
            val noteId = newNoteRef.key ?: return Resource.Error("Falha ao gerar ID da nota.")

            val noteMap = mapOf(
                "id" to noteId,
                "title" to note.title,
                "content" to note.content,
                "spaceID" to note.spaceID,
                "spaceTitle" to spaceTitle,
                "createdAt" to note.createdAt,
                "updatedAt" to note.updatedAt
            )

            // Multi-path update: save to both /spaces/{spaceId}/notes/{noteId} and /noteHistory/{noteId}
            val updates = hashMapOf<String, Any>(
                "/users/$uid/spaces/${note.spaceID}/notes/$noteId" to noteMap,
                "/users/$uid/noteHistory/$noteId" to noteMap
            )

            database.reference.updateChildren(updates).await()

            Resource.Success(null)

        } catch (e: Exception) {
            Log.d("NotesRepository", "Erro ao salvar a nota: ${e.message}")
            Resource.Error("Erro ao salvar a nota: ${e.message}")
        } as Resource<Nothing>
    }

    override suspend fun getNoteById(spaceId: String, noteId: String): Resource<Note> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val noteRef = database.reference
                .child("users")
                .child(uid)
                .child("spaces")
                .child(spaceId)
                .child("notes")
                .child(noteId)

            val snapshot = noteRef.get().await()

            if (!snapshot.exists()) {
                return Resource.Error("Nota não encontrada.")
            }

            val note = snapshot.getValue(Note::class.java)
                ?: return Resource.Error("Erro ao converter a nota.")

            Resource.Success(note)

        } catch (e: Exception) {
            Resource.Error("Erro ao buscar nota: ${e.message}")
        }
    }

    override suspend fun editNoteById(spaceId: String, noteId: String, updatedNote: Note): Resource<Nothing> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val noteRef = database.reference
                .child("users")
                .child(uid)
                .child("spaces")
                .child(spaceId)
                .child("notes")
                .child(noteId)

            val snapshot = noteRef.get().await()
            if (!snapshot.exists()) {
                return Resource.Error("Nota não encontrada.")
            }

            val updatedFields = mapOf(
                "title" to updatedNote.title,
                "content" to updatedNote.content,
                "updatedAt" to updatedNote.updatedAt
            )

            noteRef.updateChildren(updatedFields).await()

            Resource.Success(null)
        } catch (e: Exception) {
            Log.d("NotesRepository", "Erro ao editar a nota: ${e.message}")
            Resource.Error("Erro ao editar a nota: ${e.message}")
        } as Resource<Nothing>
    }

    override suspend fun deleteNoteById(spaceId: String, noteId: String): Resource<Nothing> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val noteRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("spaces")
                .child(spaceId)
                .child("notes")
                .child(noteId)

            noteRef.removeValue().await()
            deleteNoteFromHistory(noteId)
            deleteLastSeenNote()

            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error("Não foi possível excluir a nota", e)
        } as Resource<Nothing>
    }

    private suspend fun deleteNoteFromHistory(noteId: String): Resource<Nothing> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val noteRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("noteHistory")
                .child(noteId)

            noteRef.removeValue().await()

            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error("Não foi possível excluir a nota", e)
        } as Resource<Nothing>
    }

    override suspend fun searchNotes(query: String): Resource<List<Note>> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val noteHistoryRef = database.reference
                .child("users")
                .child(uid)
                .child("noteHistory")

            val snapshot = noteHistoryRef.get().await()
            if (!snapshot.exists()) {
                return Resource.Success(emptyList())
            }

            val notes = snapshot.children.mapNotNull { it.getValue(Note::class.java) }

            val filteredNotes = notes.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
            }

            Resource.Success(filteredNotes)
        } catch (e: Exception) {
            Resource.Error("Erro ao buscar notas: ${e.message}")
        }
    }

    override suspend fun getLastSeenNote(): Note? {
        val lastNote = lastSeenNoteDao.getLastSeenNote()
        return lastNote?.toNote()
    }

    override suspend fun saveLastSeenNoteLocally(note: Note) {
        Log.d("NotesRepository", "Salvando nota localmente: $note")
        lastSeenNoteDao.saveLastSeenNote(
            LastSeenNoteEntity(
                localId = 0,
                id = note.id,
                spaceId = note.spaceID,
                title = note.title,
                content = note.content,
                spaceTitle = note.spaceTitle,
                createdAt = note.createdAt,
            )
        )
    }

    override suspend fun deleteLastSeenNote() {
        lastSeenNoteDao.clearLastSeenNote()
    }
}