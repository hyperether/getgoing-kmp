package com.hyperether.getgoing_kmp.android.presentation.scenes.activities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class ActivitiesViewModel() : ViewModel() {

    var meters = mutableStateOf(4329)
        private set

    val calories = mutableStateOf(calculateCalories(meters.value))
    val walkedValue= mutableStateOf(232)

    fun setMeters(newMeters: Int) {
        meters.value = newMeters
        calories.value = calculateCalories(newMeters)
    }

    private fun calculateCalories(meters: Int): Int {
        return (meters * 0.084).roundToInt()
    }

    fun walkingTime(meters: Int): Int = (meters / 100).toInt()
    fun runningTime(meters: Int): Int = (meters / 200).toInt()
    fun cyclingTime(meters: Int): Int = (meters / 300).toInt()
    fun saveChanges(){

    }
}