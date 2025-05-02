package edu.alisson.anota.presentation.ui.spaces

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.domain.model.Space
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

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _selectedColor = MutableStateFlow(Color.Blue)
    val selectedColor = _selectedColor.asStateFlow()

    // Error states
    private val _titleError = MutableStateFlow<String?>(null)
    val titleError = _titleError.asStateFlow()

    private val _descriptionError = MutableStateFlow<String?>(null)
    val descriptionError = _descriptionError.asStateFlow()

    // UI Events
    private val _eventFlow = MutableSharedFlow<SignUpUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun onSelectedColorChange(value: Color) {
        _selectedColor.value = value
    }

    fun clearForm() {
        _title.value = ""
        _description.value = ""
        _selectedColor.value = Color.Blue
        _titleError.value = null
        _descriptionError.value = null
    }

    fun loadColor(uid: String) {
        viewModelScope.launch {
//            val hex = authRepository.getUserColor(uid)
//            if (hex != null) {
//                selectedColor = hexToColor(hex)
//            }
        }
    }

    fun saveSpace() {
        viewModelScope.launch {
            val title = _title.value
            val description = _description.value
            val color = _selectedColor.value

            val currentTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())


            spaceRepository.saveSpace(
                SpaceRequestResponse(
                    id = "",
                    title = title,
                    description = description,
                    color = color.toHex(),
                    notes = null,
                    createdAt = currentTimestamp,
                    updatedAt = currentTimestamp,
                )
            )
        }
    }
}