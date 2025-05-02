package edu.alisson.anota.presentation.navigation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import edu.alisson.anota.presentation.navigation.MainScaffold
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.home.HomeScreen
import edu.alisson.anota.presentation.ui.note.NoteScreen
import edu.alisson.anota.presentation.ui.profile.ProfileScreen
import edu.alisson.anota.presentation.ui.search.AppSearchBar
import edu.alisson.anota.presentation.ui.space.SpaceDetailsScreen
import edu.alisson.anota.presentation.ui.spaces.SpacesScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Home.route, route = "main") {
        composable(Screen.Home.route) {
            MainScaffold(
                navController = navController
            ) {
                HomeScreen(
                    navController = navController
                )
            }
        }

        composable(Screen.Pesquisar.route) {
            MainScaffold(
                navController = navController
            ) {
                AppSearchBar()
            }
        }

        composable(Screen.Perfil.route) {
            MainScaffold(
                navController = navController
            ) {
                ProfileScreen(
                    navController = navController
                )
            }
        }

        composable(Screen.Spaces.route) {
            MainScaffold(
                navController = navController
            ) {
                SpacesScreen(
                    navController = navController
                )
            }
        }

        composable(
            route = Screen.SpaceDetails.route,
            arguments = listOf(
                navArgument("spaceId") { nullable = false }
            )
        ) { backStackEntry ->
            val spaceId = backStackEntry.arguments?.getString("spaceId")!!

            MainScaffold(navController = navController) {
                SpaceDetailsScreen(
                    spaceId = spaceId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }


        composable(Screen.NoteCreate.route) {
            MainScaffold(navController = navController) {
                NoteScreen(
                    noteId = null,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.NoteDetails.route,
            arguments = listOf(
                navArgument("noteId") { nullable = false }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")!!

            MainScaffold(navController = navController) {
                NoteScreen(
                    noteId = noteId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}