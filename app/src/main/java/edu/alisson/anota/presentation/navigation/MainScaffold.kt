package edu.alisson.anota.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.alisson.anota.presentation.components.BottomNavBar

@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            content()
        }
    }

}