package com.hyperether.getgoing_kmp.android.presentation.scenes.details

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.presentation.scenes.tracking.NodeLists
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Node
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

    var locationState = mutableStateOf(LatLng(0.0, 0.0))
    val listOfGreenPoly = mutableStateOf(listOf<LatLng>())
    val listOfYellowPoly = mutableStateOf(listOf<LatLng>())
    val listOfOrangePoly = mutableStateOf(listOf<LatLng>())
    val listOfRedPoly = mutableStateOf(listOf<LatLng>())

    fun setExercise(id: Int) {
        selectedExercise.value = ExerciseType.entries.find { it.id == id }?.value ?: ""
    }

    fun selectRoute(id: Long) {
        routes.value.find { it.id == id }?.let {
            selectedRouteProgress.floatValue = it.length.toFloat() / it.goal.toFloat()

            selectedRouteCal.value = String.format("%.02f", it.energy)
            selectedRouteDistance.value = String.format("%.02f", it.length)
            selectedRouteSpeed.value = String.format("%.02f", it.avgSpeed)

            setMap(it.id)
        }
    }

    init {
        viewModelScope.launch {
            repository.getLastRoute()?.let {
                selectedRouteProgress.floatValue = it.length.toFloat() / it.goal.toFloat()

                selectedRouteCal.value = String.format("%.02f", it.energy)
                selectedRouteDistance.value = String.format("%.02f", it.length)
                selectedRouteSpeed.value = String.format("%.02f", it.avgSpeed)

                setMap(it.id)
            }

        }

        viewModelScope.launch {
            routes.value = repository.getAllRoutes()
        }
    }

    private fun setMap(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val nodes = repository.getAllNodesById(id)
            locationState.value = LatLng(nodes.first().latitude, nodes.first().longitude)
            drawRoute(nodes)
        }
    }

    private fun drawRoute(mRoute: List<Node>?) {
        if (mRoute == null) return

        var drFirstPass = true
        var firstNode: Node? = null
        var secondNode: Node? = null

        val data = NodeLists(
            mutableListOf<LatLng>(),
            mutableListOf<LatLng>(),
            mutableListOf<LatLng>(),
            mutableListOf<LatLng>()
        )

        // Redraw the whole route
        val it = mRoute.iterator()
        while (it.hasNext()) {
            if (drFirstPass) {
                firstNode = it.next().also { secondNode = it }
                drFirstPass = false
            } else {
                firstNode = secondNode
                secondNode = it.next()
            }

            if (firstNode?.isLast == true) {
                continue
            }
            drawSegment(firstNode!!, secondNode!!, data)
        }
        listOfGreenPoly.value = data.listOfGreenPoly
        listOfYellowPoly.value = data.listOfYellowPoly
        listOfOrangePoly.value = data.listOfOrangePoly
        listOfRedPoly.value = data.listOfRedPoly
    }


    private fun drawSegment(firstNode: Node, secondNode: Node, data: NodeLists) {
        if (secondNode.velocity <= 1) {
            data.listOfGreenPoly.add(LatLng(firstNode.latitude, firstNode.longitude))
            data.listOfGreenPoly.add(LatLng(secondNode.latitude, secondNode.longitude))
        } else if ((secondNode.velocity > 1) && (secondNode.velocity <= 2)) {
            data.listOfYellowPoly.add(LatLng(firstNode.latitude, firstNode.longitude))
            data.listOfYellowPoly.add(LatLng(secondNode.latitude, secondNode.longitude))
        } else if ((secondNode.velocity > 2) && (secondNode.velocity <= 3)) {
            data.listOfOrangePoly.add(LatLng(firstNode.latitude, firstNode.longitude))
            data.listOfOrangePoly.add(LatLng(secondNode.latitude, secondNode.longitude))
        } else {
            data.listOfRedPoly.add(LatLng(firstNode.latitude, firstNode.longitude))
            data.listOfRedPoly.add(LatLng(secondNode.latitude, secondNode.longitude))
        }
    }
}