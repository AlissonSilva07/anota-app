package edu.alisson.anota.presentation.ui.space

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.space.components.SpaceNoteItem
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpaceDetailsScreen(
    modifier: Modifier = Modifier,
    spaceId: String,
    navController: NavController,
    spaceDetailsScreenViewModel: SpaceDetailsScreenViewModel = hiltViewModel()
) {
    val spaceData by spaceDetailsScreenViewModel.spaceData.collectAsState()
    val spaceDataResponse by spaceDetailsScreenViewModel.spaceDataResponse.collectAsState()

    LaunchedEffect(Unit) {
        spaceDetailsScreenViewModel.getSpaceById(spaceId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
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
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is Resource.Loading<*> -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is Resource.Success<*> -> {
                    if (spaceData != null) {
                        val notes = spaceData?.notes.orEmpty()
                        val lastIndex = notes.lastIndex

                        Text(
                            text = if (notes.isNotEmpty()) "Todas as notas: ${notes.size}" else "Nada ainda por aqui. Adicione a sua primeira nota!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            itemsIndexed(notes) { index, note ->
                                SpaceNoteItem(
                                    note = note,
                                    onItemClick = {
                                        navController.navigate(Screen.NoteDetails.createRoute(
                                            spaceId = spaceId,
                                            noteId = note.id
                                        ))
                                    }
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
            navController = NavController(LocalContext.current)
        )
    }
}