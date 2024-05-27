package com.hyperether.getgoing_kmp.android

import android.content.SharedPreferences
import com.hyperether.getgoing_kmp.Constants
import com.hyperether.getgoing_kmp.Constants.PREF_RIDE_ROUTE_EXISTING
import com.hyperether.getgoing_kmp.Constants.PREF_RUN_ROUTE_EXISTING
import com.hyperether.getgoing_kmp.Constants.PREF_WALK_ROUTE_EXISTING


object SharedPref {
    private val settings: SharedPreferences =
        App.appCtxt().getSharedPreferences(Constants.PREF_FILE, 0)

    var gender: Int
        get() = settings.getInt("gender", 0)
        set(gender) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("gender", gender)
            editor.apply()
        }

    var age: Int
        get() = settings.getInt("age", 0)
        set(age) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("age", age)
            editor.apply()
        }

    var weight: Int
        get() = settings.getInt("weight", 0)
        set(weight) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("weight", weight)
            editor.apply()
        }

    var height: Int
        get() = settings.getInt("height", 0)
        set(height) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("height", height)
            editor.apply()
        }

    val isGenderSet: Boolean
        get() = settings.contains("gender")

    val isGoalSet: Boolean
        get() = settings.contains("goal")

    var goal: Int
        get() = settings.getInt("goal", 5000)
        set(goal) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("goal", goal)
            editor.apply()
        }

    fun setWalkRouteExisting(walk: Boolean) {
        val editor: SharedPreferences.Editor = settings.edit()
        editor.putBoolean(PREF_WALK_ROUTE_EXISTING, walk)
        editor.apply()
    }

    fun doesWalkRouteExist(): Boolean {
        return settings.getBoolean(PREF_WALK_ROUTE_EXISTING, false)
    }

    fun setRunRouteExisting(run: Boolean) {
        val editor: SharedPreferences.Editor = settings.edit()
        editor.putBoolean(PREF_RUN_ROUTE_EXISTING, run)
        editor.apply()
    }

    fun doesRunRouteExist(): Boolean {
        return settings.getBoolean(PREF_RUN_ROUTE_EXISTING, false)
    }

    fun setRideRouteExisting(ride: Boolean) {
        val editor: SharedPreferences.Editor = settings.edit()
        editor.putBoolean(PREF_RIDE_ROUTE_EXISTING, ride)
        editor.apply()
    }

    fun doesRideRouteExist(): Boolean {
        return settings.getBoolean(PREF_RIDE_ROUTE_EXISTING, false)
    }

    var isZeroNodeInit: Boolean
        get() = settings.getBoolean("zeroNode", false)
        set(zeroNode) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putBoolean("zeroNode", zeroNode)
            editor.apply()
        }

    var measurementSystemId: Int
        get() = settings.getInt("measurementSystemId", Constants.METRIC)
        set(id) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("meteringActivityRequestedId", id)
            editor.apply()
        }

    var lastTime: Long
        get() = settings.getLong("gg_last_time", 0)
        set(time) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putLong("gg_last_time", time)
            editor.apply()
        }

    var backgroundStartTime: Long
        get() = settings.getLong("gg_background_time", 0)
        set(currentTimeMillis) {
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putLong("gg_background_time", currentTimeMillis)
            editor.apply()
        }
}
