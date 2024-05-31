package com.hyperether.getgoing_kmp.android.util

import android.content.Context
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object Conversion {
    /**
     * Convert number of milliseconds into form HH:mm:ss
     *
     * @param seconds elapsed time
     */
    fun getDurationString(seconds: Long): String {
        var seconds = seconds
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        seconds = seconds % 60

        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" +
                twoDigitString(seconds)
    }

    /**
     * Formatting method for time output
     *
     * @param number time
     */
    fun twoDigitString(number: Long): String {
        if (number == 0L) {
            return "00"
        }

        if (number / 10 == 0L) {
            return "0$number"
        }

        return number.toString()
    }

    /**
     * This method only works if the points are close enough that you can omit that
     * earth is not regular shape
     *
     * @param lat_a first point lat
     * @param lng_a first point lng
     * @param lat_b second point lat
     * @param lng_b second point lng
     */
    fun gps2m(lat_a: Double, lng_a: Double, lat_b: Double, lng_b: Double): Double {
        val pk = 180 / 3.14169

        val a1 = lat_a / pk
        val a2 = lng_a / pk
        val b1 = lat_b / pk
        val b2 = lng_b / pk

        val t1 = cos(a1) * cos(a2) * cos(b1) * cos(b2)
        val t2 = cos(a1) * sin(a2) * cos(b1) * sin(b2)
        val t3 = sin(a1) * sin(b1)
        val tt = acos(t1 + t2 + t3)

        return 6366000 * tt
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun convertPixelToDp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
