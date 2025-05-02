package edu.alisson.anota.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import edu.alisson.anota.R
import edu.alisson.anota.data.utils.Resource
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.components.CustomImage
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {

    val userData by profileScreenViewModel.userData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userData?.let { user ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomImage(
                        base64Image = user.profileImage,
                        imgSize = 80
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = user.name ?: "Nome",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user.email ?: "Email",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
        CustomButton(
            title = "Sair",
            variant = ButtonVariant.DEFAULT,
            disabled = false,
            onClick = {
                profileScreenViewModel.logout()
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true } // clears the whole backstack
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPrev() {
    AnotaTheme {
        ProfileScreen(
            navController = NavController(LocalContext.current)
        )
    }
}