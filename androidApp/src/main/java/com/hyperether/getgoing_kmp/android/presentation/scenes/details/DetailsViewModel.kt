package com.hyperether.getgoing_kmp.android.presentation.scenes.details

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val selectedExercise = mutableStateOf("")

    val routes = mutableStateOf<List<Route>>(listOf())

    var selectedRouteProgress = mutableFloatStateOf(0.0f)
    var selectedRouteCal = mutableStateOf("")
    var selectedRouteDistance = mutableStateOf("")
    var selectedRouteSpeed = mutableStateOf("")

    fun setExercise(id: Int) {
        selectedExercise.value = ExerciseType.entries.find { it.id == id }?.value ?: ""
    }

    fun selectRoute(id: Long) {
        routes.value.find { it.id == id }?.let {
            selectedRouteProgress.floatValue = it.length.toFloat() / it.goal.toFloat()

            selectedRouteCal.value = String.format("%.02f", it.energy)
            selectedRouteDistance.value = String.format("%.02f", it.length)
            selectedRouteSpeed.value = String.format("%.02f", it.avgSpeed)
        }
    }

    init {
        viewModelScope.launch {
            repository.getLastRoute()?.let {
                selectedRouteProgress.floatValue = it.length.toFloat() / it.goal.toFloat()

                selectedRouteCal.value = String.format("%.02f", it.energy)
                selectedRouteDistance.value = String.format("%.02f", it.length)
                selectedRouteSpeed.value = String.format("%.02f", it.avgSpeed)
            }

        }

        viewModelScope.launch {
            routes.value = repository.getAllRoutes()
        }
    }
}