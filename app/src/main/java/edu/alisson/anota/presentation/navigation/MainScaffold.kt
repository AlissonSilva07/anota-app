package edu.alisson.anota.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.alisson.anota.data.Constants.spaces
import edu.alisson.anota.presentation.components.BottomNavBar
import edu.alisson.anota.presentation.ui.note.NoteIntent
import edu.alisson.anota.presentation.ui.search.AppSearchBar
import edu.alisson.anota.presentation.ui.space.SpaceDetailsScreenViewModel
import edu.alisson.anota.presentation.utils.ObserveAsEvents
import edu.alisson.anota.presentation.utils.SnackbarController
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val fabRoutes = listOf(
        Screen.Home.route, Screen.SpaceDetails.route
    )
    var menuExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            snackbarHostState.showSnackbar(
                message = event.message,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            if (currentDestination == Screen.Spaces.route) {
                TopAppBar(
                    title = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Meus Espaços",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (currentDestination == Screen.SpaceDetails.route) {
                val spaceDetailsScreenViewModel: SpaceDetailsScreenViewModel = hiltViewModel()
                val spaceData by spaceDetailsScreenViewModel.spaceData.collectAsState()
                var isDeleteDialogOpen by remember {
                    mutableStateOf(false)
                }
                TopAppBar(
                    title = {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = spaceData?.title ?: "Título",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = spaceData?.description ?: "Descrição",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Voltar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                menuExpanded = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Ações",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            offset = androidx.compose.ui.unit.DpOffset(x = (-24).dp, y = 0.dp),
                            containerColor = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Excluir",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    isDeleteDialogOpen = true
                                }
                            )
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
                                            text = "Deseja realmente excluir o espaço?",
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
                                                    spaceDetailsScreenViewModel.deleteSpace(
                                                        onSuccess = {
                                                            menuExpanded = false
                                                            isDeleteDialogOpen = false
                                                            navController.popBackStack()
                                                        },
                                                        spaceId = spaceData?.id ?: ""
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
                )
            } else if (currentDestination == Screen.Perfil.route) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Perfil",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            } else if (currentDestination == Screen.Pesquisar.route) {
                AppSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController
                )
            } else if (currentDestination == Screen.SpaceCreate.route) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Novo Espaço",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Voltar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            } else null
        }, floatingActionButton = {
            if (currentDestination in fabRoutes) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        navController.navigate(Screen.NoteCreate.route)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add, contentDescription = "Nova Nota"
                        )
                        Text(
                            text = "Nova Nota", style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else null
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            content()
        }
    }

}