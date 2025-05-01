package edu.alisson.anota.domain.repository

import com.google.firebase.auth.FirebaseUser
import edu.alisson.anota.data.utils.Resource

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser?>
    suspend fun login(email: String, password: String): Resource<FirebaseUser?>
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}