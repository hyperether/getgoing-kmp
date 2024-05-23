package com.hyperether.getgoing_kmp.di

import com.hyperether.getgoing_kmp.repository.GgRepository

class AppContainer(
    private val factory: Factory
) {
    val appRepository: GgRepository by lazy {
        GgRepository(
            factory.getRoomDatabase(),
        )
    }
}