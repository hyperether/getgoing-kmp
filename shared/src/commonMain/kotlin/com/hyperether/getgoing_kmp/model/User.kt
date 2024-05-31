package com.hyperether.getgoing_kmp.model

import com.hyperether.getgoing_kmp.repository.room.UserEntity

data class User(
    var id: Long = 0,
    var gender: UserGender = UserGender.Male,
    var age: Int = 0,
    var height: Int = 0,
    var weight: Int = 0,
    var totalKm: Double = 0.00,
    var totalKcal: Int = 0
) {
    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            gender = gender.ordinal,
            age = age,
            height = height,
            weight = weight,
            totalKm = totalKm,
            totalKcal = totalKcal
        )
    }
}

enum class UserGender {
    Male,
    Female,
    Other
}