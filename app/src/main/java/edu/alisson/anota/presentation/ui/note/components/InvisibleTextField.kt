package edu.alisson.anota.presentation.ui.note.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

enum class InvisibleTextFieldType {
    TITLE,
    BODY
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InvisibleTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String? = null,
    type: InvisibleTextFieldType
) {
    var offsetX by remember { mutableStateOf(0f) }

    LaunchedEffect(error) {
        if (error != null) {
            offsetX = 0f

            animate(
                initialValue = 0f,
                targetValue = 10f,
                animationSpec = repeatable(
                    iterations = 3,
                    animation = tween(durationMillis = 50, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.offset { IntOffset(offsetX.roundToInt(), 0) },
        textStyle = TextStyle(
            fontFamily = if (type == InvisibleTextFieldType.TITLE)
                MaterialTheme.typography.titleLarge.fontFamily
            else
                MaterialTheme.typography.bodyLarge.fontFamily,
            color = MaterialTheme.colorScheme.primary,
            fontSize = if (type == InvisibleTextFieldType.TITLE)
                MaterialTheme.typography.titleLarge.fontSize
            else
                MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = if (type == InvisibleTextFieldType.TITLE)
                FontWeight.ExtraBold
            else
                FontWeight.Normal
        ),
        maxLines = if (type == InvisibleTextFieldType.TITLE) 1 else Int.MAX_VALUE,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontFamily = if (type == InvisibleTextFieldType.TITLE)
                        MaterialTheme.typography.titleLarge.fontFamily
                    else
                        MaterialTheme.typography.bodyLarge.fontFamily,
                    color = if (error != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.secondary,
                    fontSize = if (type == InvisibleTextFieldType.TITLE)
                        MaterialTheme.typography.titleLarge.fontSize
                    else
                        MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = if (type == InvisibleTextFieldType.TITLE)
                        FontWeight.ExtraBold
                    else
                        FontWeight.Normal
                )
            }
            innerTextField()
        }
    )
}