package edu.alisson.anota.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = modifier
                    .size(80.dp),
                shape = RoundedCornerShape(percent = 100),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = modifier.size(40.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ativo(a) desde 20/08/2025",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        CustomButton(
            title = "Sair",
            variant = ButtonVariant.DEFAULT,
            disabled = false,
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPrev() {
    AnotaTheme {
        ProfileScreen()
    }
}