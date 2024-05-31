package com.hyperether.getgoing_kmp.android.location

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.SharedPref
import com.hyperether.getgoing_kmp.android.presentation.MainActivity
import com.hyperether.getgoing_kmp.android.util.CaloriesCalculation
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.toolbox.HyperNotification
import com.hyperether.toolbox.location.HyperLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GGLocationService : HyperLocationService() {

    private val repository = App.getRepository()

    companion object {
        private const val TAG = "GPSTrackingService"
        private const val ACCURACY_MIN = 20.0
    }

    private var nodeIndex: Int = 0
    private var profileID: Int = 0
    private var routeID: Long = 0
    private var weight: Double = 0.0
    private var currentRoute: Route? = null
    private var previousLocation: Location? = null
    private var previousTimestamp: Long = 0
    private var timeCumulative: Long = 0
    private var secondsCumulative: Int = 0
    private var kcalCumulative: Double = 0.0
    private var distanceCumulative: Double = 0.0
    private var velocityAvg: Double = 0.0
    private val calcCal = CaloriesCalculation()

    override fun onCreate() {
        super.onCreate()
        weight = SharedPref.weight.toDouble()
        previousTimestamp = System.currentTimeMillis()

        CoroutineScope(Dispatchers.IO).launch {
            currentRoute = repository.getLastRoute()
            currentRoute?.let {
                routeID = it.id
                profileID = it.activity_id
                distanceCumulative = it.length
                kcalCumulative = it.energy
                velocityAvg = it.avgSpeed
                timeCumulative = it.duration
                secondsCumulative = (timeCumulative / 1000).toInt()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            repository.markLastNode()
        }
    }

    override fun startForeground() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = HyperNotification.getInstance().getForegroundServiceNotification(
            this,
            getString(R.string.notification_title),
            getString(R.string.notification_text),
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            pendingIntent
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1123, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1123, notification)
        }
    }

    override fun onLocationUpdate(location: Location?) {
        location?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (it.accuracy < ACCURACY_MIN) {
                    if (previousLocation == null) {
                        previousTimestamp = System.currentTimeMillis()
                        repository.daoInsertNode(createNode(it))
                    } else {
                        val elapsedTime = System.currentTimeMillis() - previousTimestamp
                        previousTimestamp = System.currentTimeMillis()
                        timeCumulative += elapsedTime
                        secondsCumulative = (timeCumulative / 1000).toInt()

                        val distance = it.distanceTo(previousLocation!!)
                        if (distance > 0) {
                            distanceCumulative += distance
                            velocityAvg = distanceCumulative / secondsCumulative

                            val velocity = (it.speed + (distance / elapsedTime)) / 2
                            val kcalCurrent =
                                calcCal.calculate(
                                    distance.toDouble(),
                                    velocity.toDouble(), profileID, weight
                                )
                            kcalCumulative += kcalCurrent

                            currentRoute?.let { route ->
                                route.length = distanceCumulative
                                route.energy = kcalCumulative
                                route.currentSpeed = velocity.toDouble()
                                route.avgSpeed = velocityAvg

                                repository.daoInsertNode(createNode(it))
                                repository.updateRoute(route)

                            }
                        }
                    }
                    previousLocation = it
                }
            }
        }
    }

    private fun createNode(location: Location): Node {
        return Node(
            0, location.latitude, location.longitude,
            location.speed, nodeIndex++.toLong(), routeId = routeID
        )
    }
}