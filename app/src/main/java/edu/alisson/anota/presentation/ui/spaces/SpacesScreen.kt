package edu.alisson.anota.presentation.ui.spaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.alisson.anota.data.Constants.spaces
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.components.CustomColorPicker
import edu.alisson.anota.presentation.components.CustomInput
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.home.components.SpaceItem
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    spacesScreenViewModel: SpacesScreenViewModel = hiltViewModel()
) {

    val spacesData by spacesScreenViewModel.spacesData.collectAsState()
    val spacesDataResponse by spacesScreenViewModel.spacesDataResponse.collectAsState()

    LaunchedEffect(Unit) {
        spacesScreenViewModel.getAllSpaces()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.SpaceCreate.route)
                    },
                ) {
                    Text(
                        text = "Criar Novo Espaço",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
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
        SpacesScreen(
            navController = NavController(LocalContext.current),
        )
    }
}