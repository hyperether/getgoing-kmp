package com.hyperether.getgoing_kmp.android.presentation.scenes.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.GgRepository

class DetailsViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val selectedExercise = mutableStateOf("")

    fun setExercise(id: Int) {
        selectedExercise.value = ExerciseType.entries.find { it.id == id }?.value ?: ""
    }
}