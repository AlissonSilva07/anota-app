package edu.alisson.anota.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import edu.alisson.anota.presentation.components.BottomNavBar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val currentDestination = navController.currentDestination?.route
    val currentDestinationName = currentDestination?.substringAfterLast("/")?.capitalize(Locale.getDefault()).toString()

    Scaffold(
        topBar = {
            if (currentDestination == Screen.Perfil.route) {
                TopAppBar(
                    title = {
                        Text(
                            text = currentDestinationName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
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