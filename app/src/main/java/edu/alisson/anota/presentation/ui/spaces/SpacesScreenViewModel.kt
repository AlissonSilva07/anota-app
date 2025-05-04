package edu.alisson.anota.presentation.ui.spaces

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.data.mappers.toSpace
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.domain.model.User
import edu.alisson.anota.domain.repository.SpaceRepository
import edu.alisson.anota.presentation.ui.signup.SignUpUiEvent
import edu.alisson.anota.presentation.utils.toHex
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SpacesScreenViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository,
) : ViewModel() {

    private val _spacesData = MutableStateFlow<List<Space>?>(null)
    val spacesData = _spacesData.asStateFlow()

    private val _spacesDataResponse = MutableStateFlow<Resource<List<SpaceRequestResponse>?>>(Resource.Loading())
    val spacesDataResponse = _spacesDataResponse.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SignUpUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllSpaces() {
        viewModelScope.launch {
            try {
                val result = spaceRepository.getAllSpaces()

                when (result) {
                    is Resource.Success -> {
                        _spacesData.value = result.data?.map { it.toSpace() }
                        _spacesDataResponse.value = Resource.Success(result.data)
                    }
                    is Resource.Error<*> -> {
                        _spacesDataResponse.value = Resource.Error("Erro ao buscar espaços.")
                    }
                    is Resource.Loading<*> -> {
                    }
                }
            } catch (e: Exception) {
                _spacesDataResponse.value = Resource.Error(e.message ?: "Erro ao buscar espaços.")
                Log.e("HomeScreenViewModel", "Exception: ${e.message}")
            }
        }
    }
}