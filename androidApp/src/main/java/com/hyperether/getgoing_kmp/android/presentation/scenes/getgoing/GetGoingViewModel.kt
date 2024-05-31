package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.repository.GgRepository
import kotlinx.coroutines.launch

class GetGoingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val exerciseState = mutableStateOf("")
    var user: User? = null
    private var selectedExercise = ExerciseType.WALKING

    init {
        viewModelScope.launch {
            repository.getAllUsersFlow().collect {
                if (it.isNotEmpty()) {
                    user = it[0] // for now we only have one user
                }
            }
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