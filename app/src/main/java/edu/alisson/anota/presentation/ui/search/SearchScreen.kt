package edu.alisson.anota.presentation.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.presentation.ui.search.components.CardSearchItem
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val textFieldState = remember { TextFieldState() }
    val searchResults = listOf(
        Note(
            id = "1",
            title = "Grocery List",
            content = "Buy milk, eggs, and bread.",
            spaceID = "space_1",
            spaceTitle = "Personal",
            createdAt = "2025-04-01T09:00:00",
            updatedAt = "2025-04-01T09:15:00"
        ),
        Note(
            id = "2",
            title = "Meeting Notes",
            content = "Discussed Q2 roadmap and deliverables.",
            spaceID = "space_2",
            spaceTitle = "Work",
            createdAt = "2025-04-02T14:30:00",
            updatedAt = "2025-04-02T15:00:00"
        ),
        Note(
            id = "3",
            title = "Workout Plan",
            content = "Monday: Chest & Triceps. Tuesday: Back & Biceps.",
            spaceID = "space_1",
            spaceTitle = "Personal",
            createdAt = "2025-04-03T07:45:00",
            updatedAt = "2025-04-03T08:00:00"
        ),
        Note(
            id = "4",
            title = "Books to Read",
            content = "Clean Code, Atomic Habits, Deep Work.",
            spaceID = "space_3",
            spaceTitle = "Learning",
            createdAt = "2025-04-04T10:10:00",
            updatedAt = "2025-04-04T10:30:00"
        ),
        Note(
            id = "5",
            title = "App Ideas",
            content = "Note sharing app with space tagging.",
            spaceID = "space_2",
            spaceTitle = "Work",
            createdAt = "2025-04-05T11:00:00",
            updatedAt = "2025-04-05T11:20:00"
        ),
        Note(
            id = "6",
            title = "Birthday Gift Ideas",
            content = "Smartwatch, headphones, book subscription.",
            spaceID = "space_1",
            spaceTitle = "Personal",
            createdAt = "2025-04-06T13:00:00",
            updatedAt = "2025-04-06T13:25:00"
        ),
        Note(
            id = "7",
            title = "Project Todo",
            content = "Implement login, create dashboard, write tests.",
            spaceID = "space_2",
            spaceTitle = "Work",
            createdAt = "2025-04-07T08:15:00",
            updatedAt = "2025-04-07T08:45:00"
        ),
        Note(
            id = "8",
            title = "Travel Checklist",
            content = "Passport, tickets, charger, clothes.",
            spaceID = "space_1",
            spaceTitle = "Personal",
            createdAt = "2025-04-08T17:00:00",
            updatedAt = "2025-04-08T17:30:00"
        ),
        Note(
            id = "9",
            title = "Recipe: Pancakes",
            content = "Flour, milk, eggs, sugar, baking powder.",
            spaceID = "space_3",
            spaceTitle = "Learning",
            createdAt = "2025-04-09T07:30:00",
            updatedAt = "2025-04-09T07:40:00"
        ),
        Note(
            id = "10",
            title = "Learning Goals",
            content = "Master Kotlin, explore Jetpack Compose, build a portfolio.",
            spaceID = "space_3",
            spaceTitle = "Learning",
            createdAt = "2025-04-10T16:20:00",
            updatedAt = "2025-04-10T16:45:00"
        )
    )


    var expanded by rememberSaveable { mutableStateOf(true) }

    fun onSearch(query: String) {
        val results = searchResults.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .semantics { isTraversalGroup = true },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Toque para pesquisar") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                dividerColor = MaterialTheme.colorScheme.outline,
            )
        ) {
            if (textFieldState.text.isEmpty() && searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(searchResults) { note ->
                        CardSearchItem(
                            note = note,
                            onClick = {}
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            } else {
                Text(
                    text = "Nenhum resultado encontrado.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPrev() {
    AnotaTheme {
        SearchScreen()
    }
}