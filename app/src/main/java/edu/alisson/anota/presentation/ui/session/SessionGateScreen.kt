package edu.alisson.anota.presentation.ui.session

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SessionGateScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val isUserLoggedIn by sessionViewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        when (isUserLoggedIn) {
            true -> navController.navigate("main") {
                popUpTo("session") { inclusive = true }
            }
            false -> navController.navigate("auth") {
                popUpTo("session") { inclusive = true }
            }
            null -> {}
        }
    }
}