package edu.alisson.anota.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home")
    data object Pesquisar : Screen("pesquisar")
    data object Perfil : Screen("perfil")
    data object Spaces : Screen("spaces")
    data object SpaceDetails : Screen("spaces/{spaceId}") {
        fun createRoute(spaceId: String): String = "spaces/$spaceId"
    }
    data object NoteCreate : Screen("new-note")
    data object NoteDetails : Screen("spaces/{spaceId}/note/{noteId}") {
        fun createRoute(spaceId: String, noteId: String): String = "spaces/$spaceId/note/$noteId"
    }
}