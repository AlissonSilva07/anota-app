package edu.alisson.anota.presentation.components

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

fun base64ToByteArray(base64String: String): ByteArray {
    return Base64.decode(base64String, Base64.DEFAULT)
}

@Composable
fun CustomImage(
    base64Image: String?,
    imgSize: Int
) {
    val iconSize = imgSize / 2

    Box(
        modifier = Modifier
            .size(imgSize.dp)
            .clip(CircleShape)
    ) {
        if (base64Image != null) {
            val byteArray = base64ToByteArray(base64Image)

            AsyncImage(
                model = byteArray,
                contentDescription = "User profile picture",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Person Icon",
                    modifier = Modifier
                        .size(iconSize.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}