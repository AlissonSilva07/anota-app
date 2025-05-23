package edu.alisson.anota.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_6
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.home.components.CardLastNote
import edu.alisson.anota.presentation.ui.home.components.HomeHeader
import edu.alisson.anota.presentation.ui.home.components.SpaceItem
import edu.alisson.anota.presentation.ui.spaces.SpacesScreenViewModel
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    spacesScreenViewModel: SpacesScreenViewModel = hiltViewModel()
) {
    val user by homeScreenViewModel.userData.collectAsState()
    val lastSeenNote by homeScreenViewModel.lastSeenNote.collectAsState()

    val spacesData by spacesScreenViewModel.spacesData.collectAsState()
    val spacesDataResponse by spacesScreenViewModel.spacesDataResponse.collectAsState()

    LaunchedEffect(Unit) {
        spacesScreenViewModel.getAllSpaces()
        homeScreenViewModel.getLastSeenNote()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HomeHeader(
            userData = user
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Continue de onde parou",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if (lastSeenNote != null) {
                CardLastNote(
                    onClick = {
                        lastSeenNote?.let {
                            navController.navigate(
                                Screen.NoteDetails.createRoute(
                                    spaceId = it.spaceID,
                                    noteId = it.id
                                )
                            )
                        }
                    },
                    title = lastSeenNote?.title ?: "Sem título",
                    description = lastSeenNote?.content ?: "Sem conteúdo"
                )
            } else {
                Text(
                    text = "Abra uma nota dos seus espaços.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Gerencie seus espaços",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            when (spacesDataResponse) {
                is Resource.Error<*> -> {
                    Text(
                        text = "Erro ao carregar espaços",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                is Resource.Loading<*> -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is Resource.Success<*> -> {
                    if (spacesData != null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val currentSpaces = spacesData as List<Space>
                            itemsIndexed(currentSpaces) { index, space ->
                                SpaceItem(
                                    space = space,
                                    onItemClick = {
                                        navController.navigate(Screen.SpaceDetails.createRoute(spaceId = space.id))
                                    }
                                )
                                if (index < currentSpaces.lastIndex) {
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
                            text = "Não existem espaços criados.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    device = PIXEL_6
)
@Composable
private fun HomeScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        HomeScreen(
            navController = NavController(LocalContext.current),
        )
    }
}