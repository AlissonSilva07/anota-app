package edu.alisson.anota.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.dto.NoteLabelResponse
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.repository.SpaceRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : SpaceRepository {
    override suspend fun saveSpace(space: SpaceRequestResponse) {
        val uid = firebaseAuth.currentUser?.uid ?: return

        val spacesRef = database.reference
            .child("users")
            .child(uid)
            .child("spaces")

        val newSpaceRef = spacesRef.push()
        val spaceId = newSpaceRef.key ?: return

        val notesMap = mutableMapOf<String, Any>()
        space.notes?.forEach { note ->
            val newNoteRef = newSpaceRef.child("notes").push()
            val noteId = newNoteRef.key ?: return@forEach
            notesMap[noteId] = mapOf(
                "id" to noteId,
                "title" to note.title,
                "content" to note.content,
                "spaceID" to spaceId,
                "spaceTitle" to space.title,
                "createdAt" to note.createdAt,
                "updatedAt" to note.updatedAt
            )
        }

        val spaceMap = mapOf(
            "id" to spaceId,
            "title" to space.title,
            "description" to space.description,
            "color" to space.color,
            "createdAt" to space.createdAt,
            "updatedAt" to space.updatedAt,
            "notes" to notesMap
        )

        newSpaceRef.setValue(spaceMap)
    }

    override suspend fun getAllSpaces(): Resource<List<SpaceRequestResponse>?> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val spacesRef = database.reference.child("users").child(uid).child("spaces")
            val snapshot = spacesRef.get().await()

            if (!snapshot.exists()) return Resource.Success(emptyList())

            val spaces = snapshot.children.map { spaceSnap ->
                val id = spaceSnap.child("id").getValue(String::class.java) ?: ""
                val title = spaceSnap.child("title").getValue(String::class.java) ?: ""
                val description = spaceSnap.child("description").getValue(String::class.java) ?: ""
                val color = spaceSnap.child("color").getValue(String::class.java) ?: ""
                val createdAt = spaceSnap.child("createdAt").getValue(String::class.java) ?: ""
                val updatedAt = spaceSnap.child("updatedAt").getValue(String::class.java)

                val notesSnapshot = spaceSnap.child("notes")
                val notes = if (notesSnapshot.exists()) {
                    notesSnapshot.children.mapNotNull { noteSnap ->
                        noteSnap.getValue(Note::class.java)
                    }
                } else null

                SpaceRequestResponse(
                    id = id,
                    title = title,
                    description = description,
                    color = color,
                    notes = notes,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }

            Resource.Success(spaces)

        } catch (e: Exception) {
            Resource.Error("Erro ao buscar espaços: ${e.message}")
        }
    }

    override suspend fun getAllSpaceLabels(): Resource<List<NoteLabelResponse>?> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val spacesRef = database.reference
                .child("users")
                .child(uid)
                .child("spaces")

            val snapshot = spacesRef.get().await()

            if (!snapshot.exists()) {
                return Resource.Success(emptyList())
            }

            val labels = snapshot.children.mapNotNull { spaceSnap ->
                val spaceId = spaceSnap.key ?: return@mapNotNull null // Get the space ID
                val title = spaceSnap.child("title").getValue(String::class.java) ?: return@mapNotNull null

                NoteLabelResponse(
                    id = spaceId,
                    label = title
                )
            }

            Resource.Success(labels)

        } catch (e: Exception) {
            Resource.Error("Erro ao buscar espaços: ${e.message}")
        }
    }

    override suspend fun getSpaceById(spaceId: String): Resource<SpaceRequestResponse> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Resource.Error("Usuário não autenticado.")

            val snapshot = database.reference
                .child("users")
                .child(uid)
                .child("spaces")
                .child(spaceId)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Resource.Error("Espaço não encontrado.")
            }

            val id = snapshot.child("id").getValue(String::class.java) ?: ""
            val title = snapshot.child("title").getValue(String::class.java) ?: ""
            val description = snapshot.child("description").getValue(String::class.java) ?: ""
            val color = snapshot.child("color").getValue(String::class.java) ?: ""
            val createdAt = snapshot.child("createdAt").getValue(String::class.java) ?: ""
            val updatedAt = snapshot.child("updatedAt").getValue(String::class.java)

            val notesSnapshot = snapshot.child("notes")
            val notes = if (notesSnapshot.exists()) {
                notesSnapshot.children.mapNotNull { noteSnap ->
                    noteSnap.getValue(Note::class.java)
                }
            } else null

            val space = SpaceRequestResponse(
                id = id,
                title = title,
                description = description,
                color = color,
                notes = notes,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            Resource.Success(space)

        } catch (e: Exception) {
            Resource.Error("Erro ao buscar espaço: ${e.message}")
        }
    }
}