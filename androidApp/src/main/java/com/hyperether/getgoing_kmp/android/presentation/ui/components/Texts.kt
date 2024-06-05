package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoldMediumText(text: String, color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        text = text,
        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
    )
}

@Composable
fun MediumText(text: String, color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        text = text,
        style = TextStyle.Default.copy(fontSize = 20.sp)
    )
}

@Composable
fun LargeText(text: String, color: Color = MaterialTheme.colorScheme.tertiary) {
    Text(
        text = text,
        style = TextStyle.Default.copy(fontSize = 20.sp)
    )
}

@Composable
fun HeadlineText(
    textId: Int
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        text = stringResource(textId),
        style = MaterialTheme.typography.headlineSmall,
    )
}