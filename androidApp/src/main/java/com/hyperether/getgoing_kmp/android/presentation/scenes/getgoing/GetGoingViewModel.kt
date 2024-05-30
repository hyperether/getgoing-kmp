package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.GgRepository

class GetGoingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val exerciseState = mutableStateOf<String>("")
    private var selectedExercise = ExerciseType.WALKING

    fun selectExercise(exercise: ExerciseType) {
        selectedExercise = exercise
        exerciseState.value = exercise.value
    }

    fun getSelectedExerciseId(): Int {
        return selectedExercise.id
    }
}