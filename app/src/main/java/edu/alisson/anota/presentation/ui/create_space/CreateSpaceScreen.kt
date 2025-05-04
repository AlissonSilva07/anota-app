package edu.alisson.anota.presentation.ui.create_space

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.components.CustomColorPicker
import edu.alisson.anota.presentation.components.CustomInput
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun CreateSpaceScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    createSpaceScreenViewModel: CreateSpaceScreenViewModel = hiltViewModel(),
) {
    val isLoading by createSpaceScreenViewModel.isLoading.collectAsState()
    val title by createSpaceScreenViewModel.title.collectAsState()
    val description by createSpaceScreenViewModel.description.collectAsState()
    val selectedColor by createSpaceScreenViewModel.selectedColor.collectAsState()

    val titleError by createSpaceScreenViewModel.titleError.collectAsState()
    val descriptionError by createSpaceScreenViewModel.descriptionError.collectAsState()

    Column(
        modifier = modifier
            .padding(top = 0.dp, bottom = 16.dp, end = 16.dp, start = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Insira os detalhes para criar um novo espaço:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Normal
            )
            CustomInput(
                label = "Título",
                value = title,
                onValueChange = { newValue ->
                    createSpaceScreenViewModel.onTitleChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                error = titleError
            )
            CustomInput(
                label = "Descrição",
                value = description,
                onValueChange = { newValue ->
                    createSpaceScreenViewModel.onDescriptionChange(newValue)
                },
                modifier = Modifier.fillMaxWidth(),
                error = descriptionError
            )
            CustomColorPicker(
                initialColor = selectedColor
            ) { selectedColor ->
                createSpaceScreenViewModel.onSelectedColorChange(selectedColor)
            }
        }

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            title = "Salvar",
            variant = ButtonVariant.DEFAULT,
            disabled = isLoading,
            icon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            onClick = {
                createSpaceScreenViewModel.saveSpace(
                    onSuccess = {
                        createSpaceScreenViewModel.clearForm()
                        navController.popBackStack()
                    }
                )
            }
        )
    }
}

@Preview
@Composable
private fun CreateSpaceScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        CreateSpaceScreen(
            navController = NavController(LocalContext.current)
        )
    }
}