package edu.alisson.anota.presentation.ui.signup

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.alisson.anota.presentation.components.ButtonVariant
import edu.alisson.anota.presentation.components.CustomButton
import edu.alisson.anota.presentation.components.CustomInput
import edu.alisson.anota.presentation.components.PhotoPicker
import edu.alisson.anota.presentation.components.TextFieldType
import java.io.File

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpScreenViewModel: SignUpScreenViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit = {},
) {
    val context = LocalContext.current

    val name by signUpScreenViewModel.name.collectAsState()
    val email by signUpScreenViewModel.email.collectAsState()
    val password by signUpScreenViewModel.password.collectAsState()
    val passwordRepeat by signUpScreenViewModel.passwordRepeat.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let {
                val file = uriToFile(context, it)
                val base64 = uriToBase64(context, it)
                signUpScreenViewModel.onProfileImageChange(base64)
            }
        }
    )

    LaunchedEffect(Unit) {
        signUpScreenViewModel.eventFlow.collect { event ->
            when (event) {
                is SignUpUiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                SignUpUiEvent.NavigateToLogin -> navigateToLogin()
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
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhotoPicker(
                selectedImageUri,
                singlePhotoPickerLauncher
            )
            CustomInput(
                label = "Nome",
                value = name,
                onValueChange = { newValue ->
                    signUpScreenViewModel.onNameChange(newValue)
                },
                modifier = Modifier.fillMaxWidth()
            )
            CustomInput(
                label = "Email",
                value = email,
                onValueChange = { newValue ->
                    signUpScreenViewModel.onEmailChange(newValue)
                },
                modifier = Modifier.fillMaxWidth()
            )
            CustomInput(
                label = "Senha",
                value = password,
                onValueChange = { newValue ->
                    signUpScreenViewModel.onPasswordChange(newValue)
                },
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )
            CustomInput(
                label = "Repetir Senha",
                value = passwordRepeat,
                onValueChange = { newValue ->
                    signUpScreenViewModel.onPasswordRepeatChange(newValue)
                },
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )
            CustomButton(
                title = "Cadastrar",
                variant = ButtonVariant.DEFAULT,
                disabled = false,
                onClick = {
                    signUpScreenViewModel.signUp()
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                onClick = { navigateToLogin() },
            ) {
                Text(
                    text = "Já tem conta? Faça login",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

fun uriToFile(context: Context, uri: Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val tempFile = File.createTempFile("profile_pic", ".jpg", context.cacheDir)
    inputStream.use { input -> tempFile.outputStream().use { output -> input.copyTo(output) } }
    return tempFile
}

fun uriToBase64(context: Context, uri: Uri): String? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val byteArray = inputStream.readBytes()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}