package com.hyperether.getgoing_kmp.android.presentation.scenes.tracking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.location.GGLocationService
import com.hyperether.getgoing_kmp.android.util.Conversion
import com.hyperether.getgoing_kmp.android.util.ExerciseType
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import com.hyperether.getgoing_kmp.util.Constants
import com.hyperether.toolbox.location.HyperLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("MissingPermission")
class TrackingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {

    val trackingStarted = mutableStateOf(false)

    //todo move to utils
    val formatter = SimpleDateFormat("dd.MM.yyyy.' 'HH:mm:ss", Locale.ENGLISH)

    private var timerScope = CoroutineScope(Dispatchers.IO + Job())
    private var timeInt = 0L

    var locationState = mutableStateOf(LatLng(0.0, 0.0))
    val listOfGreenPoly = mutableStateOf(listOf<LatLng>())
    val listOfYellowPoly = mutableStateOf(listOf<LatLng>())
    val listOfOrangePoly = mutableStateOf(listOf<LatLng>())
    val listOfRedPoly = mutableStateOf(listOf<LatLng>())

    val durationState = mutableStateOf("")
    val caloriesState = mutableStateOf("")
    val distanceState = mutableStateOf("")
    val velocityState = mutableStateOf("")

    val selectedExercise = mutableStateOf("")

    init {
        viewModelScope.launch {
            startLocationUpdates()
        }

        caloriesState.value = String.format("%.02f kcal", 0.0)
        distanceState.value = String.format("%.02f m", 0.0)
        velocityState.value = String.format("%.02f m/s", 0.0)
        durationState.value = Conversion.getDurationString(0L)
    }

    fun startLocationUpdates() {
        val client = LocationServices.getFusedLocationProviderClient(App.appCtxt())

        client.lastLocation.addOnSuccessListener {
            locationState.value = LatLng(it.latitude, it.longitude)
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    locationState.value = LatLng(location.latitude, location.longitude)
                }
            }
        }
        val locationRequest: LocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        )
            .setMinUpdateIntervalMillis(500)
            .build()

        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    var routeId = -1L
    fun startTracking() {
        trackingStarted.value = true
        if (routeId.toInt() == -1) {
            viewModelScope.launch {
                val activityId =
                    ExerciseType.entries.find { it.value == selectedExercise.value }?.id ?: 0
                val route =
                    Route(0, 0, 0.0, 0.0, formatter.format(Date()), 0.0, 0.0, activityId, 2000)
                repository.insertRoute(route, object : RouteAddedCallback {
                    override fun onRouteAdded(id: Long) {
                        routeId = id
                        startServiceAndTimer()
                        CoroutineScope(Dispatchers.IO).launch {
                            startObservingNodes()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            startObservingRoute()
                        }
                    }
                })
            }
        } else {
            startServiceAndTimer()
        }
    }

    private fun startServiceAndTimer() {
        val intent = Intent(
            App.appCtxt(),
            GGLocationService::class.java
        )
        intent.putExtra(
            HyperLocationService.LOC_INTERVAL,
            Constants.UPDATE_INTERVAL
        )
        intent.putExtra(
            HyperLocationService.LOC_FASTEST_INTERVAL,
            Constants.FASTEST_INTERVAL
        )
        intent.putExtra(
            HyperLocationService.LOC_DISTANCE,
            Constants.LOCATION_DISTANCE
        )
        App.appCtxt().startService(intent)
        startTimer()
    }

    fun stopTracking() {
        trackingStarted.value = false
        cancelTimer()
        App.appCtxt().stopService(Intent(App.appCtxt(), GGLocationService::class.java))
    }

    private suspend fun startObservingNodes() {
        repository.getAllNodesByIdFlow(routeId).collect {
            drawRoute(it)
        }
    }

    private suspend fun startObservingRoute() {
        repository.getRouteByIdFlow(routeId).collect {
            caloriesState.value = String.format("%.02f kcal", it.energy)
            distanceState.value = String.format("%.02f m", it.length)
            velocityState.value = String.format("%.02f m/s", it.currentSpeed)
        }
    }

    private fun startTimer() {
        Log.d("Start timer", "start")
        timerScope = CoroutineScope(Dispatchers.IO + Job())
        timerScope.launch {
            while (true) {
                delay(1000L)
                timeInt++
                durationState.value = Conversion.getDurationString(timeInt)
            }
        }
    }

    private fun cancelTimer() {
        Log.d("Start timer", "stop")
        timerScope.cancel()
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
                Log.i("LAST", "${firstNode.isLast}")
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

    fun setExercise(id: Int) {
        selectedExercise.value = ExerciseType.entries.find { it.id == id }?.value ?: ""
    }
}

data class NodeLists(
    val listOfGreenPoly: MutableList<LatLng>,
    val listOfYellowPoly: MutableList<LatLng>,
    val listOfOrangePoly: MutableList<LatLng>,
    val listOfRedPoly: MutableList<LatLng>
)