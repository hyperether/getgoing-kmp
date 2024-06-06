package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.ui.theme.GetgoingkmpTheme

@Composable
fun LinkWithIcon(text: String, icon: ImageVector? = null, click: () -> Unit = {}) {
    Row(
        Modifier.clickable { click() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle.Default.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Icon(
            imageVector = icon ?: Icons.Filled.KeyboardArrowRight,
            contentDescription = "Button icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp)
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

@Composable
fun PlayButton(isRunning: Boolean = false, click: () -> Unit) {
    IconButton(
        onClick = { click() },
        Modifier
            .size(100.dp)
            .padding(1.dp)
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = if (isRunning) painterResource(id = R.drawable.ic_pouse) else painterResource(
                id = R.drawable.ic_play
            ),
            contentDescription = "Play button",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun BackButton(onNavigateBack: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(60.dp)
            .padding(bottom = 1.dp),
        onClick = { onNavigateBack() }) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ConfirmButton(onClick: () -> Unit) {
    TextButton(
        onClick = {
            onClick()
        }
    ) {
        Text("Confirm")
    }
}

@Composable
fun DismissButton(onClick: () -> Unit) {
    TextButton(
        onClick = {
            onClick()
        }
    ) {
        Text("Cancel")
    }
}

@Preview
@Composable
private fun ButtonsPreview() {
    GetgoingkmpTheme {
        Column {
            PlayButton {

            }
        }
    }
}