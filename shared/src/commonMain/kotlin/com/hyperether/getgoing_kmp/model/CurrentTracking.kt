package com.hyperether.getgoing_kmp.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CurrentTracking {
    var routeId: Long = -1
        internal set

    var time: MutableStateFlow<Long> = MutableStateFlow(0L)

    var selectedExercise: Int = 0
        internal set
}
