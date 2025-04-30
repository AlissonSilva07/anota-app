package edu.alisson.anota.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.alisson.anota.presentation.navigation.MainScaffold
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.home.HomeScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Home.route, route = "main") {
        composable(Screen.Home.route) {
            MainScaffold(
                navController = navController
            ) {
                HomeScreen()
            }
        }
    }
}