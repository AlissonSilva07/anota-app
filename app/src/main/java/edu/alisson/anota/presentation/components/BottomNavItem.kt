package edu.alisson.anota.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector
import edu.alisson.anota.presentation.navigation.Screen

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavItem(Screen.Home.route, title = "Home", Icons.Outlined.StickyNote2)
    data object Search : BottomNavItem(Screen.Search.route, title = "Pesquisar", Icons.Outlined.Search)
    data object Profile : BottomNavItem(Screen.Profile.route, title = "Perfil", Icons.Outlined.Person)
}