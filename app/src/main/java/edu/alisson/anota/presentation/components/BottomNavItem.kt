package edu.alisson.anota.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector
import edu.alisson.anota.presentation.navigation.Screen

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    data object Home : BottomNavItem(Screen.Home.route, title = "Home", Icons.Outlined.StickyNote2, Icons.Filled.StickyNote2)
    data object Search : BottomNavItem(Screen.Procurar.route, title = "Pesquisar", Icons.Outlined.Search, Icons.Filled.Search)
    data object Profile : BottomNavItem(Screen.Perfil.route, title = "Perfil", Icons.Outlined.Person, Icons.Filled.Person)
}