package com.hyperether.getgoing_kmp.android.presentation.scenes.activities

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.CaloriesCalculation
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ActivitiesViewModel(private val repository: GgRepository = App.getRepository()) : ViewModel() {

    var routeList =mutableStateOf(emptyList<Route>())
    var meters = mutableStateOf(4329)
        private set

    val calories = mutableStateOf(calculateCalories(meters.value))
    val walkedValue= mutableStateOf(232)
    private var _user = MutableStateFlow<User?>(null)
    val user
        get() = _user.asStateFlow()

    fun setMeters(newMeters: Int) {
        meters.value = newMeters
        calories.value = calculateCalories(newMeters)
        user.value?.let {
            calories.value= ((newMeters/10) * 0.0112* it.weight).toInt()
        }
    }
    fun fetchUserById(userId: Long) {
        viewModelScope.launch {
            repository.getUser(userId).collect {
                _user.value = it
                it?.let {
                    meters.value=it.goalMeters
                    calories.value=calculateCalories(it.goalMeters)
                }
            }
        }
    }

    fun fetchRouteList(){
        viewModelScope.launch {
            routeList.value=repository.getAllRoutes()
        }
    }
    private fun calculateCalories(meters: Int): Int {
        return (meters * 0.084).roundToInt()
    }

    fun saveChanges(){
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                repository.updateUser(
                    it.copy(
                        goalMeters = meters.value,
                    )
                )
            }
        }
    }
}