package com.hyperether.getgoing_kmp.android.util

import android.app.ActivityManager
import android.content.Context
import com.hyperether.getgoing_kmp.android.location.GGLocationService

object ServiceUtil {
    fun isServiceActive(context: Context): Boolean {
        val activityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services: List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(
            Int.MAX_VALUE
        )
        for (runningServiceInfo in services) {
            if (runningServiceInfo.service.getClassName() == GGLocationService::class.java.getName()) {
                return true
            }
        }
        return false
    }
}
