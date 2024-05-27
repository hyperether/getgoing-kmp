package com.hyperether.getgoing_kmp.android.util

import com.hyperether.getgoing_kmp.android.SharedPref


class CaloriesCalculation {
    // Algorithm variables
    // Lookup table for calories calculation first is speed in m/s, second is
    // kcal/m*kg
    private val kcalMatrix = arrayOf(
        doubleArrayOf(0.89, 0.00078), doubleArrayOf(1.12, 0.00074),
        doubleArrayOf(1.34, 0.00073), doubleArrayOf(1.56, 0.00071), doubleArrayOf(1.79, 0.00078),
        doubleArrayOf(2.01, 0.00087), doubleArrayOf(2.23, 0.00099), doubleArrayOf(2.68, 0.00104),
        doubleArrayOf(3.13, 0.00102), doubleArrayOf(3.58, 0.00105), doubleArrayOf(4.02, 0.00104),
        doubleArrayOf(4.47, 0.00112)
    )

    /**
     * This method calculate burned calories.
     *
     * @param dis    distance traveled
     * @param vel    velocity
     * @param weight user weight
     */
    fun calculate(dis: Double, vel: Double, profileID: Int, weight: Double): Double {
        var weight = weight
        var energySpent = 0.0
        var i: Int

        if (SharedPref.measurementSystemId == 1 ||
            SharedPref.measurementSystemId == 2
        ) weight *= 0.4536 //convert weght to metric system for calculations;


        if (profileID == 1 || profileID == 2) {
            // walking and running algorithm

            if (vel < kcalMatrix[0][0]) { // if the measured speed is lower than the
                // first entry use the first entry
                energySpent = dis * (kcalMatrix[0][1] * weight)
            } else if (vel > kcalMatrix[11][0]) { // if the measured speed is higher
                // than the last entry use the
                // last entry
                energySpent = dis * (kcalMatrix[11][1] * weight)
            } else {
                i = 1
                while (i < 12) {
                    if (vel < kcalMatrix[i][0]) { // take the next higher value
                        energySpent = dis * (kcalMatrix[i][1] * weight)
                        break
                    }
                    i++
                }
            }
        } else {    // bicycle riding is selected
            energySpent = if (vel < 4.47) {
                dis * (0.000779 * weight) // riding velocity lower than 16 km/h
            } else {
                dis * (0.001071 * weight) // riding velocity higher than 16 km/h
            }
        }

        return energySpent
    }
}
