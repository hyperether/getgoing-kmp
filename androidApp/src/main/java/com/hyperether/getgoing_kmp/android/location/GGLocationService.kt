package com.hyperether.getgoing_kmp.android.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.HandlerThread
import android.os.SystemClock
import android.util.Log
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.SharedPref
import com.hyperether.getgoing_kmp.android.utils.CaloriesCalculation
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.MapNode
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.android.ui.activity.LocationActivity
import com.hyperether.toolbox.HyperNotification
import com.hyperether.toolbox.location.HyperLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GGLocationService : HyperLocationService() {

    private val repository = App.getRepository()

    private val ACCURACY_MIN = 20.0
    private var nodeIndex: Long = 0
    private var routeID: Long = 0
    private var previousLocation: Location? = null
    private var secondsCumulative: Int = 0
    private var timeCumulative: Long = 0
    private var distanceCumulative: Double = 0.0
    private lateinit var thread: HandlerThread
    private lateinit var handler: Handler
    private var weight: Double = 0.0;
    private var previousTimeStamp: Long = 0;
    private var routeId: Long = 0
    private var profileID: Int = 0
    private var kcalCumulative = 0.0
    private var velocityAvg = 0.0
    private var currentRoute: Route? = null
    private lateinit var calculator: CaloriesCalculation
    // add calculator

    override fun onCreate() {
        super.onCreate()
        val sharedPref: SharedPref = SharedPref.newInstance()
        calculator = CaloriesCalculation.newInstance()
        weight = sharedPref.getWeight().toDouble()
        timeCumulative = SystemClock.elapsedRealtime()
        App.getHandler().post(Runnable {
            CoroutineScope(Dispatchers.IO).launch {
                currentRoute = repository.getLastRoute2()
                if (currentRoute != null) {
                    routeId = currentRoute!!.id
                    profileID = currentRoute!!.activity_id
                    distanceCumulative = currentRoute!!.length
                    kcalCumulative = currentRoute!!.energy
                    velocityAvg = currentRoute!!.avgSpeed
                    timeCumulative = currentRoute!!.duration
                    secondsCumulative = timeCumulative.toInt() / 1000
                }
            }

        })
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun startForeground() {
        super.startForeground()
        val intent = Intent(this, LocationActivity::class.java)
        val pendingIntent: PendingIntent =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        startForeground(
            1123, HyperNotification.getInstance().getForegroundServiceNotification(
                this,
                getString(R.string.notification_title),
                getString(R.string.notification_text),
                R.drawable.ic_launher,
                R.mipmap.ic_launcher,
                pendingIntent
            )
        )
        thread = HandlerThread("ggthread")
        thread.start()
        handler = Handler(thread.looper)
    }

    override fun onLocationUpdate(location: Location?) {
        handler.post {
            if (location != null && location.accuracy < ACCURACY_MIN) {
                if (previousLocation == null) {
                    previousTimeStamp = System.currentTimeMillis()
                    repository.daoInsertNode(createNode(location)) // ok
                } else {
                    val elapsedTime: Long = System.currentTimeMillis() - previousTimeStamp
                    previousTimeStamp = System.currentTimeMillis()
                    timeCumulative += elapsedTime
                    secondsCumulative = timeCumulative.toInt() / 1000
                    val distance: Float = location.distanceTo(previousLocation!!)
                    if (distance > 0) {
                        distanceCumulative += distance
                        velocityAvg = distanceCumulative / secondsCumulative
                        val velocity: Float = (location.speed + (distance / elapsedTime)) / 2
                        val kcalCurrent = calculator.calculate(
                            distance.toDouble(),
                            velocity.toDouble(),
                            profileID,
                            weight
                        )
                        kcalCumulative += kcalCurrent
                        repository.daoInsertNode(createNode(location)) // ok
                        currentRoute?.duration = timeCumulative / 1000   // its ok in seconds now
                        currentRoute?.length = distanceCumulative
                        currentRoute?.energy = kcalCumulative
                        currentRoute?.currentSpeed = velocity.toDouble()
                        currentRoute?.avgSpeed = velocityAvg
                        Log.d(
                            "From Service",
                            "vals $distanceCumulative $kcalCumulative ${velocity.toDouble()}: $velocityAvg"
                        )
                        currentRoute?.let { CoroutineScope(Dispatchers.IO).launch { repository.updateRoute(it) }  }
                    }

                }
                previousLocation = location
            }
        }
    }

    fun createNode(location: Location): MapNode {
        return MapNode(
            0,
            location.latitude,
            location.longitude,
            location.speed,
            nodeIndex++,
            routeID
        )
    }
}