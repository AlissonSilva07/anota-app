package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.data.utils.Resource

interface SpaceRepository {
    suspend fun saveSpace(space: SpaceRequestResponse)
    suspend fun getAllSpaces(): Resource<List<SpaceRequestResponse>?>
}