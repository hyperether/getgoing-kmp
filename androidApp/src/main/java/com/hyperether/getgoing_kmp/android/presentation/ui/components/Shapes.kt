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

class GGShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = Path().apply {
                moveTo(size.width / 4, size.height - 72f)
                cubicTo(
                    size.width / 2 - 100, size.height - 50,
                    size.width / 2 - 160, size.height - 30,
                    size.width / 2, size.height
                )
                cubicTo(
                    size.width - size.width / 2 + 160,
                    size.height - 30,
                    size.width - size.width / 2 + 100,
                    size.height - 50,
                    size.width - size.width / 4,
                    size.height - 72
                )
                lineTo(
                    size.width,
                    size.height - 72
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
                    size.height - 72
                )
                lineTo(size.width / 4, size.height - 72f)
                close()
            }
        )
    }
}

@Preview
@Composable
private fun PreviewShape() {
    Color.Red
    Box(modifier = Modifier
        .size(300.dp)
        .clip(GGShape())
        .background(Color.Red))
}