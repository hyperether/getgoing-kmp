package com.hyperether.getgoing_kmp.android.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun generateGGPath(size: Size) = Path().apply {
    val ydiff = 90f
    moveTo(size.width / 5, size.height - ydiff)
    cubicTo(
        size.width / 2 - 100, size.height - 85,
        size.width / 2 - 160, size.height - 5,
        size.width / 2, size.height
    )
    cubicTo(
        size.width - size.width / 2 + 160,
        size.height - 5,
        size.width - size.width / 2 + 100,
        size.height - 85,
        size.width - size.width / 5,
        size.height - ydiff
    )
    lineTo(
        size.width,
        size.height - ydiff
    )

    lineTo(
        size.width,
        0f
    )

    lineTo(
        0f,
        0f
    )
    lineTo(
        0f,
        size.height - ydiff
    )
    lineTo(size.width / 4, size.height - ydiff)
    close()
}

class GGShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = generateGGPath(size)
        )
    }
}

@Preview
@Composable
private fun PreviewShape() {
    Color.Red
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(GGShape())
            .background(Color.Red)
    )
}