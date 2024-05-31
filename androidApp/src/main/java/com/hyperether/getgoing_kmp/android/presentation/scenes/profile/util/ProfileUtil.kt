package com.hyperether.getgoing_kmp.android.presentation.scenes.profile.util

import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.presentation.scenes.profile.components.Items
import com.hyperether.getgoing_kmp.model.UserGender
import java.util.Locale

object ProfileUtil {

    fun getUserGender(gender: UserGender?): UserGender {
        return gender ?: UserGender.Male
    }

    fun getUserAge(age: Int?): Int {
        return age?.takeIf { it != 0 } ?: 1
    }

    fun getUserWeight(weight: Int?): Int {
        return weight?.takeIf { it != 0 } ?: 40
    }

    fun getUserHeight(height: Int?): Int {
        return height?.takeIf { it != 0 } ?: 110
    }

    fun formatGender(gender: UserGender?): String {
        return gender?.name ?: UserGender.Male.name
    }

    fun formatAge(age: Int?): String {
        return "${age ?: 0} years"
    }

    fun formatHeight(height: Int?): String {
        return "${height ?: 0}cm"
    }

    fun formatWeight(weight: Int?): String {
        return "${weight ?: 0}kg"
    }

    fun formatTotalKm(totalKm: Double?): String {
        return String.format(Locale.getDefault(), "%.02f km", totalKm ?: 0.0)
    }

    fun formatTotalKcal(totalKcal: Int?): String {
        return "${totalKcal ?: 0} kcal"
    }

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