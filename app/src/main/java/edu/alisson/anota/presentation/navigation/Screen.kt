package edu.alisson.anota.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home")
    data object Procurar : Screen("procurar")
    data object Perfil : Screen("perfil")
    data object Create : Screen("create")
}