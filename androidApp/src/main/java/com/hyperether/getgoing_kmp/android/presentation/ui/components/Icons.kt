package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hyperether.getgoing_kmp.android.R

@Composable
fun Logo(size: Dp = 60.dp) {
    Icon(
        modifier = Modifier.size(size),
        painter = painterResource(id = R.drawable.ic_logo_light),
        contentDescription = "Logo icon",
        tint = Color.Unspecified,
    )
}

@Composable
fun Profile(size: Dp = 60.dp, click: () -> Unit = {}) {
    IconButton(onClick = { click() }, modifier = Modifier.size(size).padding(0.dp)) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Profile icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(size),
        )
    }
}
