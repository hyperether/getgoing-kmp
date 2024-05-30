package com.hyperether.getgoing_kmp.android.presentation.scenes.profile.util

import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.components.Items
import com.hyperether.getgoing_kmp.model.UserGender

object ProfileUtil {

    fun generateAgeList(): List<Items> {
        return (1..120).map { Items(it) }
    }

    fun generateHeightList(): List<Items> {
        return (110..250).map { Items(it) }
    }

    fun generateWeightList(): List<Items> {
        return (40..150).map { Items(it) }
    }

    fun getUserGenderIcon(gender: UserGender?): Int {
        return when (gender?.ordinal) {
            0 -> {
                R.drawable.ic_gender_male
            }

            1 -> {
                R.drawable.ic_gender_female
            }

            2 -> {
                R.drawable.ic_gender_trans
            }

            else -> {
                R.drawable.ic_gender_male
            }
        }
    }
}