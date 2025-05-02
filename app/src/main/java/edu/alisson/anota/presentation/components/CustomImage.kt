package edu.alisson.anota.presentation.components

import android.util.Base64
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.ByteArrayInputStream
import java.io.InputStream

fun base64ToInputStream(base64String: String): InputStream {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return ByteArrayInputStream(decodedBytes)
}

@Composable
fun CustomImage(
    base64String: String?,
    imgSize: Int
) {
    base64String?.let { base64 ->
        val inputStream = base64ToInputStream(base64)

        AsyncImage(
            model = inputStream,
            contentDescription = null,
            modifier = Modifier.size(imgSize.dp),
            placeholder = coil.compose.rememberImagePainter("your_placeholder_image"),
            error = coil.compose.rememberImagePainter("error_image"),
            onSuccess = {
                // Handle image loaded successfully
            },
            onError = {
                // Handle image load failure
            }
        )
    }
}