package edu.alisson.anota.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.repository.NotesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class NotesRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
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

            newNoteRef.setValue(noteMap).await()

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
}