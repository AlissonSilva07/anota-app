package edu.alisson.anota.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.alisson.anota.presentation.navigation.graph.authGraph
import edu.alisson.anota.presentation.navigation.graph.mainGraph
import edu.alisson.anota.presentation.ui.session.SessionGateScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "session"
    ) {
        composable("session") {
            SessionGateScreen(navController)
        }
        authGraph(navController)
        mainGraph(navController)
    }
}