package edu.alisson.anota.data.repository


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase,
) : AuthRepository {

    private fun uriConvert(context: Context, uri: Uri): String? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override suspend fun signUpWithImage(
        name: String,
        email: String,
        password: String,
        base64Image: String?
    ): Resource<FirebaseUser?> {
        return try {
            // Create user
            Log.d("AuthRepositoryImpl", "$email, $password")

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user


            if (firebaseUser == null) {
                return Resource.Error("Erro ao criar usuário")
            }

            val uid = firebaseUser.uid

            // Prepare user data
            val userMap = mapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "profileImage" to base64Image
            )

            // Save user data to Realtime Database
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

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}