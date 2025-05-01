package edu.alisson.anota.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.components.CustomInput
import edu.alisson.anota.presentation.components.TextFieldType
import edu.alisson.anota.presentation.ui.theme.AnotaTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    loginViewModel: LoginScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val emailError by loginViewModel.emailError.collectAsState()
    val passwordError by loginViewModel.passwordError.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                UiEvent.NavigateToHome -> navigateToHome()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.StickyNote2,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = "Boas-vindas ao Anota!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomInput(
                label = "Email",
                value = email,
                onValueChange = { newValue ->
                    loginViewModel.onEmailChange(newValue)
                },
                error = emailError,
                modifier = Modifier.fillMaxWidth()
            )
            CustomInput(
                label = "Senha",
                value = password,
                onValueChange = {newValue ->
                    loginViewModel.onPasswordChange(newValue)
                },
                error = passwordError,
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )
            CustomButton(
                title = "Entrar",
                variant = ButtonVariant.DEFAULT,
                disabled = false,
                onClick = { loginViewModel.login() },
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                onClick = { navigateToSignUp() },
            ) {
                Text(
                    text = "Não tem conta? Cadastre-se",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    AnotaTheme(
        dynamicColor = false
    ) {
        LoginScreen(
            navigateToHome = {},
            navigateToSignUp = {}
        )
    }
}