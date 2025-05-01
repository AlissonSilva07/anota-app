package edu.alisson.anota.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result.user)
        } catch (e: Exception) {
            Resource.Error("Erro ao registrar usuário", e)
        }
    }

    override suspend fun login(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user)
        } catch (e: Exception) {
            Resource.Error("Erro ao fazer login", e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}