package com.hyperether.getgoing_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform