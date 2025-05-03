package edu.alisson.anota.presentation.ui.space

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.alisson.anota.data.Constants.spaces
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.presentation.ui.space.components.SpaceNoteItem
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceDetailsScreen(
    modifier: Modifier = Modifier,
    spaceId: String,
    navigateBack: () -> Unit,
    spaceDetailsScreenViewModel: SpaceDetailsScreenViewModel = hiltViewModel()
) {
    val spaceData by spaceDetailsScreenViewModel.spaceData.collectAsState()
    val spaceDataResponse by spaceDetailsScreenViewModel.spaceDataResponse.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("SpaceDetailsScreen", "LaunchedEffect: $spaceId")
        spaceDetailsScreenViewModel.getSpaceById(spaceId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (spaceDataResponse) {
                is Resource.Error<*> -> {
                    Text(
                        text = "Não foi possível recuperar as notas deste espaço.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                is Resource.Loading<*> -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                is Resource.Success<*> -> {
                    if (spaceData != null) {
                        val notes = spaceData?.notes.orEmpty()
                        val lastIndex = notes.lastIndex

                        Text(
                            text = "Todas as notas: ${notes.size ?: 0}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                        ) {
                            itemsIndexed(notes) { index, note ->
                                SpaceNoteItem(
                                    note = note,
                                    onItemClick = {}
                                )
                                if (index < lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Nenhuma nota disponível",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(vertical = 32.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpacesScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        SpaceDetailsScreen(
            spaceId = "1",
            navigateBack = {}
        )
    }
}