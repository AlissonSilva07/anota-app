package edu.alisson.anota.presentation.ui.note.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

enum class InvisibleTextFieldType {
    TITLE,
    BODY
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvisibleTextField(
    placeholder: String,
    modifier: Modifier = Modifier,
    type: InvisibleTextFieldType
) {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontSize = if (type == InvisibleTextFieldType.TITLE) MaterialTheme.typography.titleLarge.fontSize else MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = if (type == InvisibleTextFieldType.TITLE) FontWeight.ExtraBold else FontWeight.Normal
        ),
        maxLines = if (type == InvisibleTextFieldType.TITLE) 1 else Int.MAX_VALUE,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = if (type == InvisibleTextFieldType.TITLE) MaterialTheme.typography.titleLarge.fontSize else MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = if (type == InvisibleTextFieldType.TITLE) FontWeight.ExtraBold else FontWeight.Normal
                )
            }
            innerTextField()
        }
    )
}