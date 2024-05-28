package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonTextIcon(text: String, icon: ImageVector? = null, click: () -> Unit = {}) {
    TextButton(
        onClick = { click() }, modifier = Modifier
            .defaultMinSize(1.dp)
            .padding(1.dp)
    ) {
        Text(
            text = text,
            style = TextStyle.Default.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Icon(
            imageVector = icon ?: Icons.Filled.KeyboardArrowRight,
            contentDescription = "Button icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PrimaryButton(text: String, click: () -> Unit) {
    Button(
        onClick = { click() },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = text)
    }
}