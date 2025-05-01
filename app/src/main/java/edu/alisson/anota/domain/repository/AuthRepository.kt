package edu.alisson.anota.domain.repository

import com.google.firebase.auth.FirebaseUser
import edu.alisson.anota.data.utils.Resource

interface AuthRepository {
    suspend fun signUpWithImage(
        name: String,
        email: String,
        password: String,
        base64Image: String?
    ): Resource<FirebaseUser?>
    suspend fun login(email: String, password: String): Resource<FirebaseUser?>
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}