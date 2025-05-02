package edu.alisson.anota.presentation.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.alisson.anota.data.utils.DataStoreManager
import edu.alisson.anota.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    private val _uid = MutableStateFlow<String?>(null)
    val uid = _uid.asStateFlow()

    init {
        _isUserLoggedIn.value = authRepository.getCurrentUser() != null

        viewModelScope.launch {
            dataStoreManager.uidFlow.collect {
                _uid.value = it
            }
        }
    }
}