package com.hyperether.getgoing_kmp.android.util

import com.hyperether.getgoing_kmp.model.User

object UserUtil {

    fun isUserCreated(user: User?): Boolean {
        return user?.run { age > 0 && height > 0 && weight > 0 } ?: false
    }

    fun getUserId(user: User?): Long {
        return user?.id ?: 0L
    }
}