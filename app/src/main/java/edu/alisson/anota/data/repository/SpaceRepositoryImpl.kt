package edu.alisson.anota.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.domain.repository.SpaceRepository
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
): SpaceRepository {
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

    override suspend fun getUserColor(uid: String): String? {
        TODO("Not yet implemented")
    }
}