package com.hyperether.getgoing_kmp.di

import com.hyperether.getgoing_kmp.repository.room.AppDatabase

expect class Factory {
    fun getRoomDatabase(): AppDatabase
}