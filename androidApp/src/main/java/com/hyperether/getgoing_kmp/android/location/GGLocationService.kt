package com.hyperether.getgoing_kmp.android.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.SharedPref
import com.hyperether.getgoing_kmp.android.presentation.MainActivity
import com.hyperether.getgoing_kmp.android.util.CaloriesCalculation
import com.hyperether.getgoing_kmp.android.util.Conversion
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.toolbox.location.HyperLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GGLocationService : HyperLocationService() {

    private val repository = App.getRepository()

    companion object {
        private const val TAG = "GPSTrackingService"
        private const val ACCURACY_MIN = 20.0
        private const val CHANNEL_ID = "Service Channel"
    }

    var job: Job? = null

    private var nodeIndex: Int = 0
    private var profileID: Int = 0
    private var routeID: Long = repository.getCurrentTracking().routeId
    private var weight: Double = 0.0
    private var previousLocation: Location? = null
    private var previousTimestamp: Long = 0
    private var timeCumulative: Long = 0
    private var secondsCumulative: Int = 0
    private var kcalCumulative: Double = 0.0
    private var distanceCumulative: Double = 0.0
    private var velocityAvg: Double = 0.0
    private val calcCal = CaloriesCalculation()

    private val timerJob = SupervisorJob()
    private val timerScope = CoroutineScope(Dispatchers.IO + timerJob)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        weight = SharedPref.weight.toDouble()
        previousTimestamp = System.currentTimeMillis()

        timerScope.launch {
            while (true) {
                Log.d("service", "Service update timer")
                delay(1000L)
                val time = repository.getCurrentTracking().time.value + 1
                repository.updateCurrentTrackingTime(time)
                startForeground()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob.cancel()
        CoroutineScope(Dispatchers.IO).launch {
            repository.markLastNode()
            Log.d("Service thread", "Service on destroy")
            job?.cancel()
        }
    }

    private fun createNotification(time: String, distance: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(getString(R.string.duration_distance_notification_text, time, distance))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSilent(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
        return builder.build()
    }

    override fun startForeground() {
        val notification = createNotification(
            Conversion.getDurationString(repository.getCurrentTracking().time.value),
            String.format("%.02f m", repository.getCurrentTracking().distance)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1123, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1123, notification)
        }
    }

    override fun onLocationUpdate(location: Location?) {
        location?.let {
            job = CoroutineScope(Dispatchers.IO).launch {
                Log.d("Service thread", "Service still running")
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

                            repository.getLastRoute()?.let { route ->
                                route.length = distanceCumulative
                                route.energy = kcalCumulative
                                route.currentSpeed = velocity.toDouble()
                                route.avgSpeed = velocityAvg
                                repository.updateCurrentTrackingDistance(distanceCumulative)
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}