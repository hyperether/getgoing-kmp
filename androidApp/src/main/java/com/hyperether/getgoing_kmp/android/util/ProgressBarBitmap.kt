package com.hyperether.getgoing_kmp.android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.hyperether.getgoing_kmp.android.R

object ProgressBarBitmap {
    fun getWidgetBitmap(
        context: Context,
        goal: Long,
        length: Double,
        width: Int,
        height: Int,
        startAngle: Float,
        sweepAngle: Float,
        stroke: Int,
        padding: Int
    ): Bitmap {
        var scale = 0f
        if (goal > 0 && length >= 0) {
            scale = (length / goal).toFloat()
        }

        //Paint for arc stroke.
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = stroke.toFloat()
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        val arc: RectF = RectF()
        arc.set(
            ((stroke / 2) + padding).toFloat(),
            ((stroke / 2) + padding).toFloat(),
            (width - padding - (stroke / 2)).toFloat(),
            (height - padding - (stroke / 2)).toFloat()
        )

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        //draw full arc as background.
        paint.color = context.resources.getColor(R.color.black)
        canvas.drawArc(arc, startAngle, sweepAngle, false, paint)

        //draw arc progress with actual value.
        paint.color = context.resources.getColor(R.color.color_button_background)

        var temp = 0f
        temp = if (scale <= 1 && scale >= 0) {
            sweepAngle * scale
        } else if (scale < 0) {
            0f
        } else {
            sweepAngle
        }

        canvas.drawArc(arc, startAngle, temp, false, paint)

        return bitmap
    }
}
