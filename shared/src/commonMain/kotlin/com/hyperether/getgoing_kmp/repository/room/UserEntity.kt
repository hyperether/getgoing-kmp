package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.model.UserGender

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var gender: Int = 0, // 0 = male, 1 = female, 2 = other
    var age: Int = 0,
    var height: Int = 0,
    var weight: Int = 0,
    var totalKm: Double = 0.00,
    var totalKcal: Int = 0
) {
    fun toUser(): User {
        return User(
            id = id,
            gender = getUserGender(gender),
            age = age,
            height = height,
            weight = weight,
            totalKm = totalKm,
            totalKcal = totalKcal
        )
    }

    private fun getUserGender(value: Int): UserGender {
        return when (value) {
            0 -> {
                UserGender.Male
            }

            1 -> {
                UserGender.Female
            }

            2 -> {
                UserGender.Other
            }

            else -> {
                UserGender.Male
            }
        }
    }
}
