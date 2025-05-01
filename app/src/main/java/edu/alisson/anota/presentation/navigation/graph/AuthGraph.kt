package edu.alisson.anota.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.alisson.anota.presentation.navigation.AuthScaffold
import edu.alisson.anota.presentation.navigation.Screen
import edu.alisson.anota.presentation.ui.login.LoginScreen
import edu.alisson.anota.presentation.ui.signup.SignUpScreen

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(startDestination = Screen.Login.route, route = "auth") {
        composable(Screen.Login.route) {
            AuthScaffold(
                navController = navController
            ) {
                LoginScreen(
                    navigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToSignUp = {
                        navController.navigate(Screen.SignUp.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        composable(Screen.SignUp.route) {
            AuthScaffold(
                navController = navController
            ) {
                SignUpScreen(
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.SignUp.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}