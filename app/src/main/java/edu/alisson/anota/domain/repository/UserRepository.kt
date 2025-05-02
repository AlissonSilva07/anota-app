package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.User

interface UserRepository {
    suspend fun getUserData(uid: String): Resource<User>
    suspend fun getLocalUser(uid: String): User?
}