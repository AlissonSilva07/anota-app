package edu.alisson.anota.presentation.ui.space

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.data.mappers.toSpace
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.domain.repository.SpaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpaceDetailsScreenViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository,
) : ViewModel() {

    private val _spaceData = MutableStateFlow<Space?>(null)
    val spaceData = _spaceData.asStateFlow()

    private val _spaceDataResponse = MutableStateFlow<Resource<SpaceRequestResponse>>(Resource.Loading())
    val spaceDataResponse = _spaceDataResponse.asStateFlow()

    fun getSpaceById(spaceId: String) {
        viewModelScope.launch {
            try {
                val result = spaceRepository.getSpaceById(spaceId)

                when (result) {
                    is Resource.Success -> {
                        _spaceData.value = result.data.toSpace()
                        _spaceDataResponse.value = Resource.Success(result.data)
                    }
                    is Resource.Error<*> -> {
                        _spaceDataResponse.value = Resource.Error("Erro ao buscar espaços.")
                    }
                    is Resource.Loading<*> -> {
                    }
                }
            } catch (e: Exception) {
                _spaceDataResponse.value = Resource.Error(e.message ?: "Erro ao buscar espaços.")
                Log.e("HomeScreenViewModel", "Exception: ${e.message}")
            }
        }
    }
}