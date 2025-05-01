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
    data object NoteCreate : Screen("note")
    data object NoteDetails : Screen("note/{noteId}") {
        fun createRoute(noteId: String): String = "spaces/note/$noteId"
    }
}