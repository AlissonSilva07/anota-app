package edu.alisson.anota.data.repository


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : AuthRepository {

    override suspend fun signUpWithImage(
        name: String,
        email: String,
        password: String,
        base64Image: String?
    ): Resource<FirebaseUser?> {
        return try {
            Log.d("AuthRepositoryImpl", "$email, $password")

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user


            if (firebaseUser == null) {
                return Resource.Error("Erro ao criar usuário")
            }

            val uid = firebaseUser.uid

            val userMap = mapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "profileImage" to base64Image
            )

            database.reference
                .child("users")
                .child(uid)
                .setValue(userMap)
                .await()

            Resource.Success(firebaseUser)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido ao cadastrar usuário.")
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

    override suspend fun getUserData(uid: String): Resource<User> {
        return try {
            val snapshot = database.reference
                .child("users")
                .child(uid)
                .get()
                .await()

            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)

                if (user != null) {
                    Resource.Success(user)
                } else {
                    Resource.Error("User data is null")
                }
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error while fetching user data")
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}