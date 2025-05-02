package edu.alisson.anota.data.repository

import com.google.firebase.database.FirebaseDatabase
import edu.alisson.anota.data.local.UserDatabase
import edu.alisson.anota.data.mappers.toUser
import edu.alisson.anota.data.mappers.toUserEntity
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val localDatabase: UserDatabase
) : UserRepository {

    override suspend fun getUserData(uid: String): Resource<User> {
        return try {
            val localUser = localDatabase.userDao().getUser(uid)?.toUser()
            if (localUser != null) {
                return Resource.Success(localUser)
            }

            val snapshot = database.reference
                .child("users")
                .child(uid)
                .get()
                .await()

            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    localDatabase.userDao().insertUser(user.toUserEntity())
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

    override suspend fun getLocalUser(uid: String): User? {
        return localDatabase.userDao().getUser(uid)?.toUser()
    }
}
