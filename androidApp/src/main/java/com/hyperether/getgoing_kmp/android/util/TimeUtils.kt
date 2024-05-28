package com.hyperether.getgoing_kmp.android.util

import android.widget.Chronometer
import com.hyperether.getgoing_kmp.util.Constants

object TimeUtils {
    fun chronometerToMills(mChronometer: Chronometer): Long {
        var currentMilliseconds = 0
        val chronoText = mChronometer.text.toString()
        val array = chronoText.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (array.size == 2) {
            currentMilliseconds = (array[0].toInt() * 60 * 1000
                    + array[1].toInt() * 1000)
        } else if (array.size == 3) {
            currentMilliseconds =
                array[0].toInt() * 60 * 60 * 1000 + array[1].toInt() * 60 * 1000 + array[2].toInt() * 1000
        }
        return currentMilliseconds.toLong()
    }

    fun getTimeEstimates(dist: Int): IntArray {
        val returnValues = IntArray(3)

        returnValues[0] = (dist / (Constants.AVG_SPEED_WALK * 60)).toInt()
        returnValues[1] = (dist / (Constants.AVG_SPEED_RUN * 60)).toInt()
        returnValues[2] = (dist / (Constants.AVG_SPEED_CYCLING * 60)).toInt()

        return returnValues
    }
}
