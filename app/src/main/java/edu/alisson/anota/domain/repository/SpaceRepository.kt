package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.dto.SpaceRequestResponse

interface SpaceRepository {
    suspend fun saveSpace(space: SpaceRequestResponse)
    suspend fun getUserColor(uid: String): String?
}