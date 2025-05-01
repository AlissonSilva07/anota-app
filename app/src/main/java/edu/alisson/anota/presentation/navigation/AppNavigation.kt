package edu.alisson.anota.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import edu.alisson.anota.presentation.navigation.graph.authGraph
import edu.alisson.anota.presentation.navigation.graph.mainGraph

@RequiresApi(Build.VERSION_CODES.O)
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