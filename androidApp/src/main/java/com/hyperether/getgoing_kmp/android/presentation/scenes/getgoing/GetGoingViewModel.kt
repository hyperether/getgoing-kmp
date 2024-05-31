package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.model.UserGender
import com.hyperether.getgoing_kmp.repository.GgRepository
import kotlinx.coroutines.launch

class GetGoingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val exerciseState = mutableStateOf("")
    var userId: Long = 0
    private var selectedExercise = ExerciseType.WALKING

    init {
        // TODO just for testing
        viewModelScope.launch {
            userId = repository.insertUser(
                User(
                    gender = UserGender.Male,
                    age = 32,
                    height = 188,
                    weight = 84,
                    totalKm = 0.0,
                    totalKcal = 0
                )
            )
        }
    }

    fun selectExercise(exercise: ExerciseType) {
        selectedExercise = exercise
        exerciseState.value = exercise.value
    }

    fun getSelectedExerciseId(): Int {
        return selectedExercise.id
    }
}