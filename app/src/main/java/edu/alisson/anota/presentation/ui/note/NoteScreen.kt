package edu.alisson.anota.presentation.ui.note

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.alisson.anota.data.Constants.spaces
import edu.alisson.anota.data.dto.NoteLabelResponse
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.ui.note.components.InvisibleTextField
import edu.alisson.anota.presentation.ui.note.components.InvisibleTextFieldType
import edu.alisson.anota.presentation.ui.home.components.SpaceItem
import edu.alisson.anota.presentation.ui.login.LoginUiEvent
import edu.alisson.anota.presentation.ui.note.components.SpaceRadioButton
import edu.alisson.anota.presentation.ui.spaces.SpacesScreenViewModel
import edu.alisson.anota.presentation.ui.theme.AnotaTheme
import kotlin.collections.lastIndex
import kotlin.collections.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    spaceId: String? = null,
    noteId: String? = null,
    navigateBack: () -> Unit,
    notesScreenViewModel: NotesScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val intent: NoteIntent = remember(spaceId, noteId) {
        when {
            spaceId != null && noteId != null -> NoteIntent.Edit(spaceId, noteId)
            else -> NoteIntent.Create
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var isOpenModal by remember {
        mutableStateOf(false)
    }

    var isDeleteDialogOpen by remember {
        mutableStateOf(false)
    }

    val isLoading by notesScreenViewModel.isLoading.collectAsState()
    val spaceLabels by notesScreenViewModel.spaceLabels.collectAsState()
    val spaceLabelsResponse by notesScreenViewModel.spaceLabelsResponse.collectAsState()
    val selectedSpace by notesScreenViewModel.selectedSpace.collectAsState()

    val noteTitle by notesScreenViewModel.noteTitle.collectAsState()
    val noteBody by notesScreenViewModel.noteBody.collectAsState()

    val noteTitleError by notesScreenViewModel.noteTitleError.collectAsState()
    val noteBodyError by notesScreenViewModel.noteBodyError.collectAsState()

    LaunchedEffect(Unit) {
        notesScreenViewModel.eventFlow.collect { event ->
            if (event is LoginUiEvent.ShowToast) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(intent) {
        if (intent is NoteIntent.Edit) {
            notesScreenViewModel.getNoteById(intent.spaceId, intent.noteId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InvisibleTextField(
                placeholder = "Título da Nota",
                type = InvisibleTextFieldType.TITLE,
                value = noteTitle,
                error = noteTitleError,
                onValueChange = notesScreenViewModel::onChangeNoteTitle,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            InvisibleTextField(
                placeholder = "Corpo da Nota",
                type = InvisibleTextFieldType.BODY,
                value = noteBody,
                error = noteBodyError,
                onValueChange = notesScreenViewModel::onChangeNoteBody,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(56.dp),
                    shape = RoundedCornerShape(percent = 100),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline,
                        disabledContentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    enabled = intent is NoteIntent.Create,
                    onClick = {
                        isOpenModal = true
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (intent) {
                                is NoteIntent.Create -> selectedSpace?.label ?: "Selec. Espaço"
                                is NoteIntent.Edit -> selectedSpace?.label ?: "Selec. Espaço"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.widthIn(
                                max = 72.dp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.MiddleEllipsis
                        )
                        Icon(
                            imageVector = if (intent is NoteIntent.Create) Icons.Outlined.ChevronRight else Icons.Outlined.Lock,
                            contentDescription = "Selecionar Espaço",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(percent = 100),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(
                            enabled = !isLoading,
                            onClick = {
                                if (intent is NoteIntent.Create) {
                                    notesScreenViewModel.createNote(
                                        onSuccess = {
                                            navigateBack()
                                        }
                                    )
                                } else {
                                    notesScreenViewModel.editNote(
                                        onSuccess = {
                                            navigateBack()
                                        }
                                    )
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Save,
                                contentDescription = "Salvar",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                        if (intent is NoteIntent.Edit) {
                            IconButton(
                                enabled = !isLoading,
                                onClick = {
                                    isDeleteDialogOpen = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Deletar e sair",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                        IconButton(
                            enabled = !isLoading,
                            onClick = { navigateBack() },
                        ) {
                            if (!isLoading) {
                                Icon(
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = "Sair",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            } else {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isOpenModal) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isOpenModal = false },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            tonalElevation = 1.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Selecionar Espaço",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Escolha um espaço para salvar sua nota:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Normal
                )
                when (spaceLabelsResponse) {
                    is Resource.Error<*> -> {
                        Text(
                            text = "Não foi possível recuperar os espaços.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is Resource.Loading<*> -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is Resource.Success<*> -> {
                        if (spaceLabels != null) {
                            val spaceLabels = spaceLabels.orEmpty()
                            val spaceSelected = remember { mutableStateOf(spaceLabels.firstOrNull()) }

                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                itemsIndexed(spaceLabels) { index, spaceLabel ->
                                    SpaceRadioButton(
                                        modifier = modifier.fillMaxWidth(),
                                        spaceLabel = spaceLabel.label,
                                        selected = spaceSelected.value?.id == spaceLabel.id,
                                        onSelected = {
                                            spaceSelected.value = spaceLabel
                                        }
                                    )
                                    if (index < spaceLabels.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            thickness = 1.dp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }

                            CustomButton(
                                modifier = modifier.fillMaxWidth(),
                                title = "Escolher espaço",
                                variant = ButtonVariant.DEFAULT,
                                disabled = false,
                                onClick = {
                                    if (spaceSelected.value != null) {
                                        notesScreenViewModel.setSelectedSpace(spaceSelected.value!!)
                                    }
                                    isOpenModal = false
                                }
                            )
                        } else {
                            Text(
                                text = "Nenhum espaço salvo.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(vertical = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (isDeleteDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { isDeleteDialogOpen = false },
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = AlertDialogDefaults.TonalElevation,
                contentColor = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Atenção",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Deseja realmente excluir a nota?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        TextButton(
                            onClick = {
                                notesScreenViewModel.deleteNote(
                                    onSuccess = {
                                        isDeleteDialogOpen = false
                                        navigateBack()
                                    },
                                    spaceId = (intent as NoteIntent.Edit).spaceId,
                                    noteId = (intent as NoteIntent.Edit).noteId,
                                )
                            },
                        ) {
                            Text(
                                text = "Confirmar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        TextButton(
                            onClick = { isDeleteDialogOpen = false },
                        ) {
                            Text(
                                text = "Cancelar",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun CreateNoteScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        NoteScreen(
            navigateBack = {}
        )
    }
}