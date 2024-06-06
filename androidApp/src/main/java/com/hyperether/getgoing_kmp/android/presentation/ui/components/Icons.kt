package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.R

@Composable
fun Logo(size: Dp = 48.dp) {
    Icon(
        modifier = Modifier.size(size),
        painter = painterResource(id = R.drawable.ic_logo_light),
        contentDescription = "Logo icon",
        tint = Color.Unspecified,
    )
}

@Composable
fun Profile(click: () -> Unit = {}) {
    Icon(
        painter = painterResource(id = R.drawable.ic_user),
        contentDescription = "Profile icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp).clickable { click() },
    )
}