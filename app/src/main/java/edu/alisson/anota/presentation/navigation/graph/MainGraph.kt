package edu.alisson.anota.presentation.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.alisson.anota.presentation.navigation.MainScaffold
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.create.CreateNoteScreen
import edu.alisson.anota.presentation.ui.home.HomeScreen
import edu.alisson.anota.presentation.ui.profile.ProfileScreen
import edu.alisson.anota.presentation.ui.search.SearchScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Home.route, route = "main") {
        composable(Screen.Home.route) {
            MainScaffold(
                navController = navController
            ) {
                HomeScreen()
            }
        }

        composable(Screen.Procurar.route) {
            MainScaffold(
                navController = navController
            ) {
                SearchScreen()
            }
        }

        composable(Screen.Perfil.route) {
            MainScaffold(
                navController = navController
            ) {
                ProfileScreen()
            }
        }

        composable(Screen.Create.route) {
            MainScaffold(
                navController = navController
            ) {
                CreateNoteScreen(
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}