package com.hyperether.getgoing_kmp.di

import com.hyperether.getgoing_kmp.repository.GgRepositoryImpl

class AppContainer(
    private val factory: Factory
) {
    val appRepository: GgRepositoryImpl by lazy {
        GgRepositoryImpl(
            factory.getRoomDatabase(),
        )
    }
}