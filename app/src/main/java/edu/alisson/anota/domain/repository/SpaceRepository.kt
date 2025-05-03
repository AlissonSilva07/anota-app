package edu.alisson.anota.domain.repository

import edu.alisson.anota.data.dto.NoteLabelResponse
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.data.utils.Resource

interface SpaceRepository {
    suspend fun saveSpace(space: SpaceRequestResponse)
    suspend fun getAllSpaces(): Resource<List<SpaceRequestResponse>?>
    suspend fun getAllSpaceLabels(): Resource<List<NoteLabelResponse>?>
    suspend fun getSpaceById(id: String): Resource<SpaceRequestResponse>
    suspend fun deleteSpaceById(spaceId: String): Resource<Nothing>
}