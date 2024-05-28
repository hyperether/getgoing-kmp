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
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import com.hyperether.getgoing_kmp.util.Constants
import com.hyperether.toolbox.location.HyperLocationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("MissingPermission")
class TrackingViewModel(val repository: GgRepository = App.getRepository()) : ViewModel() {
    //todo move to utils
    val formatter = SimpleDateFormat("dd.MM.yyyy.' 'HH:mm:ss", Locale.ENGLISH)

    var locationState = mutableStateOf(LatLng(0.0, 0.0))

    init {
        viewModelScope.launch {
            startLocationUpdates()
        }
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
                    Log.d(
                        "MainActivity",
                        ("Location: " + location.latitude).toString() + ", " + location.longitude
                    )
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

    fun startTracking() {
        viewModelScope.launch {
            val route = Route(0, 0, 0.0, 0.0, formatter.format(Date()), 0.0, 0.0, 0, 2000)
            repository.insertRoute(route, object : RouteAddedCallback {
                override fun onRouteAdded(id: Long) {
                    //todo remember id and observe nodes from repo
                    val intent = Intent(
                        App.appCtxt(),
                        GGLocationService::class.java
                    )
                    intent.putExtra(HyperLocationService.LOC_INTERVAL, Constants.UPDATE_INTERVAL)
                    intent.putExtra(
                        HyperLocationService.LOC_FASTEST_INTERVAL,
                        Constants.FASTEST_INTERVAL
                    )
                    intent.putExtra(HyperLocationService.LOC_DISTANCE, Constants.LOCATION_DISTANCE)
                    App.appCtxt().startService(intent)
                }
            })
        }
    }
}