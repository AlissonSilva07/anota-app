package edu.alisson.anota.presentation.ui.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.dto.NoteLabelResponse
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.repository.NotesRepository
import edu.alisson.anota.domain.repository.SpaceRepository
import edu.alisson.anota.presentation.ui.login.LoginUiEvent
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
class NotesScreenViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val spaceRepository: SpaceRepository
) : ViewModel() {

    private val _spaceLabels = MutableStateFlow<List<NoteLabelResponse>?>(null)
    val spaceLabels = _spaceLabels.asStateFlow()

    private val _spaceLabelsResponse = MutableStateFlow<Resource<List<NoteLabelResponse>?>>(Resource.Loading())
    val spaceLabelsResponse = _spaceLabelsResponse.asStateFlow()

    private val _noteData = MutableStateFlow<Note?>(null)
    val noteData = _noteData.asStateFlow()

    private val _noteDataResponse = MutableStateFlow<Resource<Note?>>(Resource.Loading())
    val noteDataResponse = _noteDataResponse.asStateFlow()

    private val _selectedSpace = MutableStateFlow<NoteLabelResponse?>(null)
    val selectedSpace = _selectedSpace.asStateFlow()

    fun setSelectedSpace(spaceLabel: NoteLabelResponse) {
        _selectedSpace.value = spaceLabel
    }

    private val _noteTitle = MutableStateFlow<String>("")
    val noteTitle = _noteTitle.asStateFlow()

    fun onChangeNoteTitle(title: String) {
        _noteTitle.value = title
    }

    private val _noteBody = MutableStateFlow<String>("")
    val noteBody = _noteBody.asStateFlow()

    fun onChangeNoteBody(body: String) {
        _noteBody.value = body
    }

    private val _noteTitleError = MutableStateFlow<String?>(null)
    val noteTitleError = _noteTitleError.asStateFlow()

    private val _noteBodyError = MutableStateFlow<String?>(null)
    val noteBodyError = _noteBodyError.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getAllSpaceLabels()
    }

    fun getAllSpaceLabels() {
        viewModelScope.launch {
            try {
                val result = spaceRepository.getAllSpaceLabels()

                when (result) {
                    is Resource.Success -> {
                        _spaceLabels.value = result.data
                        _spaceLabelsResponse.value = Resource.Success(result.data)
                    }

                    is Resource.Error<*> -> {
                        _spaceLabelsResponse.value = Resource.Error("Erro ao buscar espaços.")
                    }

                    is Resource.Loading<*> -> {
                    }
                }
            } catch (e: Exception) {
                _spaceLabelsResponse.value = Resource.Error(e.message ?: "Erro ao buscar espaços.")
                Log.e("NotesScreenViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun createNote(onSuccess: () -> Unit) = viewModelScope.launch {
        if (_selectedSpace.value == null) {
            _eventFlow.emit(LoginUiEvent.ShowToast("Escolha um espaço para salvar a nota."))
        }

        if (noteTitle.value.isBlank()) {
            _noteTitleError.value = "Escolha um nome para sua nota"
        }

        if (noteBody.value.isBlank()) {
            _noteBodyError.value = "Escolha um corpo para sua nota"
        }

        try {
            val title = _noteTitle.value
            val body = _noteBody.value
            val spaceId = _selectedSpace.value?.id ?: ""
            val currentTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())

            notesRepository.saveNote(
                note = Note(
                    id = "",
                    title = title,
                    content = body,
                    spaceID = spaceId,
                    spaceTitle = _selectedSpace.value?.label ?: "",
                    createdAt = currentTimestamp,
                    updatedAt = currentTimestamp
                )
            )
            onSuccess()
        } catch (e: Exception) {
            Log.e("NotesScreenViewModel", "Exception: ${e.message}")
        }
    }

    fun getNoteById(spaceId: String, noteId: String) = viewModelScope.launch {
        try {
            val result = notesRepository.getNoteById(spaceId = spaceId, noteId = noteId)

            when (result) {
                is Resource.Success -> {
                    val note = result.data
                    _noteData.value = note
                    _noteDataResponse.value = Resource.Success(note)

                    if (note != null) {
                        _noteTitle.value = note.title
                        _noteBody.value = note.content
                        _selectedSpace.value = NoteLabelResponse(note.spaceID, note.spaceTitle)

                        saveLastSeenNote(note)
                    }
                }

                is Resource.Error<*> -> {
                    _noteDataResponse.value = Resource.Error("Erro ao buscar nota.")
                }

                is Resource.Loading<*> -> {
                    _noteDataResponse.value = Resource.Loading()
                }
            }
        } catch (e: Exception) {
            _noteDataResponse.value = Resource.Error(e.message ?: "Erro ao buscar nota.")
            Log.e("NotesScreenViewModel", "Exception: ${e.message}")
        }
    }

    fun editNote(onSuccess: () -> Unit) = viewModelScope.launch {
        if (_selectedSpace.value == null) {
            _eventFlow.emit(LoginUiEvent.ShowToast("Escolha um espaço para salvar a nota."))
        }

        if (noteTitle.value.isBlank()) {
            _noteTitleError.value = "Escolha um nome para sua nota"
        }

        if (noteBody.value.isBlank()) {
            _noteBodyError.value = "Escolha um corpo para sua nota"
        }

        try {
            val title = _noteTitle.value
            val body = _noteBody.value
            val spaceId = _selectedSpace.value?.id ?: ""
            val currentTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())

            notesRepository.editNoteById(
                spaceId = spaceId,
                noteId = _noteData.value?.id ?: "",
                updatedNote = Note(
                    id = "",
                    title = title,
                    content = body,
                    spaceID = spaceId,
                    spaceTitle = _selectedSpace.value?.label ?: "",
                    createdAt = currentTimestamp,
                    updatedAt = currentTimestamp
                )
            )
            onSuccess()
        } catch (e: Exception) {
            Log.e("NotesScreenViewModel", "Exception: ${e.message}")
        }
    }
    
    fun deleteNote(onSuccess: () -> Unit, noteId: String) = viewModelScope.launch {
        try {
            val spaceId = _selectedSpace.value?.id ?: ""

            if (spaceId.isBlank() || noteId.isBlank()) {
                return@launch
            }

            notesRepository.deleteNoteById(
                spaceId = spaceId,
                noteId = noteId
            )
            onSuccess()
        } catch (e: Exception) {
            Log.e("NotesScreenViewModel", "Exception: ${e.message}")
        }
    }

    private fun saveLastSeenNote(note: Note) {
        viewModelScope.launch {
            try {
                if (note != null) {
                    notesRepository.saveLastSeenNoteLocally(
                        note = note
                    )
                }
            } catch (e: Exception) {
                Log.e("NotesScreenViewModel", "Exception: ${e.message}")
            }
        }
    }
}