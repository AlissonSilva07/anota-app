package edu.alisson.anota.presentation.ui.search

import androidx.lifecycle.ViewModel
import edu.alisson.anota.data.Mocks.spaces
import edu.alisson.anota.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel: ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())
    val searchResults: StateFlow<List<Note>> = _searchResults.asStateFlow()

    fun onSearch(query: String) {
        _searchQuery.value = query

        val allNotes: List<Note> = spaces.flatMap { it.notes as Iterable<Note> }

        _searchResults.value = allNotes.filter { note ->
            note.title.contains(query, ignoreCase = true)
        }
    }
}