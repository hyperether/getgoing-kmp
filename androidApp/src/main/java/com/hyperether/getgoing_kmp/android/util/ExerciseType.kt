package com.hyperether.getgoing_kmp.android.util

import com.hyperether.getgoing_kmp.android.R

enum class ExerciseType(val value: String, val image: Int) {
    RUNNING(value = "Running", image = R.drawable.ic_light_running_white),
    WALKING(value = "Walking", image = R.drawable.ic_walking_white),
    CYCLING(value = "Cycling", image = R.drawable.ic_bicycling_white)
}