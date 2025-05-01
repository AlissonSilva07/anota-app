package edu.alisson.anota.presentation.components

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PhotoPicker(
    selectedImage: Uri?,
    singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
) {

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.clickable { launchPhotoPicker() },
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.secondary
            ),
            content = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (selectedImage == Uri.EMPTY) {
                        Column(
                            modifier = Modifier.size(120.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddAPhoto,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Escolha uma foto",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.surface,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        ImageLayoutView(
                            selectedImage = selectedImage,
                            selectImage = { launchPhotoPicker() }
                        )
                    }
                }
            }
        )
        if (selectedImage != Uri.EMPTY) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Clique novamente para adicionar outra foto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.surface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ImageLayoutView(
    selectedImage: Uri?,
    selectImage: () -> Unit = {}
) {
    AsyncImage(
        model = selectedImage,
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clickable { selectImage() },
        contentScale = ContentScale.Crop,
    )
}