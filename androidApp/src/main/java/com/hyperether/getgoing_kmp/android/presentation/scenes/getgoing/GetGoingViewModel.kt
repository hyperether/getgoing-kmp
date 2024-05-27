package com.hyperether.getgoing_kmp.android.presentation.scenes.getgoing

import androidx.lifecycle.ViewModel
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.repository.GgRepository

class GetGoingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {
}