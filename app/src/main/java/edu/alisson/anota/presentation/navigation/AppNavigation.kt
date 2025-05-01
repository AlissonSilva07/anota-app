package edu.alisson.anota.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import edu.alisson.anota.presentation.navigation.graph.authGraph
import edu.alisson.anota.presentation.navigation.graph.mainGraph

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}