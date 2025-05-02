package edu.alisson.anota.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.alisson.anota.data.Constants.spaces
import edu.alisson.anota.presentation.components.BottomNavBar
import edu.alisson.anota.presentation.ui.search.AppSearchBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier, navController: NavHostController, content: @Composable () -> Unit
) {
    val currentDestination = navController.currentDestination?.route
    val currentDestinationName =
        currentDestination?.substringAfterLast("/")?.capitalize(Locale.getDefault()).toString()
    val fabRoutes = listOf(
        Screen.Home.route, Screen.SpaceDetails.route
    )

    Scaffold(
        topBar = {
            if (currentDestination == Screen.Spaces.route) {
                TopAppBar(title = {
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
                }, modifier = Modifier.fillMaxWidth(), actions = {
                    IconButton(
                        onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Nova Nota",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                })
            } else if (currentDestination == Screen.SpaceDetails.route) {
                val space = spaces.first()

                TopAppBar(title = {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = space.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = space.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }, modifier = Modifier.fillMaxWidth(), navigationIcon = {
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
                }, actions = {
                    IconButton(
                        onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "Ações",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                })
            } else if (currentDestination == Screen.Perfil.route) {
                TopAppBar(
                    title = {
                        Text(
                            text = currentDestinationName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    })
            } else if (currentDestination == Screen.Pesquisar.route) {
                AppSearchBar(
                    modifier = Modifier.fillMaxWidth()
                )
            } else null
        }, floatingActionButton = {
            if (currentDestination in fabRoutes) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        navController.navigate(Screen.NoteDetails.route)
                    }) {
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
        }, bottomBar = {
            BottomNavBar(navController = navController)
        }, containerColor = MaterialTheme.colorScheme.surface
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