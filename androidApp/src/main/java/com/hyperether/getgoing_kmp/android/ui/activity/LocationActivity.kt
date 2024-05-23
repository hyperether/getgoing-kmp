package com.hyperether.getgoing_kmp.android.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.hyperether.getgoing_kmp.Constants
import com.hyperether.getgoing_kmp.Constants.OPENED_FROM_LOCATION_ACT
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.SharedPref
import com.hyperether.getgoing_kmp.android.databinding.ActivityLocationBinding
import com.hyperether.getgoing_kmp.android.location.GGLocationService
import com.hyperether.getgoing_kmp.android.ui.handler.LocationActivityClickHandler
import com.hyperether.getgoing_kmp.android.ui.handler.MainActivityClickHandler
import com.hyperether.getgoing_kmp.android.utils.ServiceUtil
import com.hyperether.getgoing_kmp.android.utils.TimeUtils
import com.hyperether.getgoing_kmp.android.viewmodel.NodeListViewModel
import com.hyperether.getgoing_kmp.android.viewmodel.RouteViewModel
import com.hyperether.getgoing_kmp.model.CBDataFrame
import com.hyperether.getgoing_kmp.repository.room.MapNode
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class LocationActivity : AppCompatActivity(), OnMapReadyCallback, RouteAddedCallback {
    val REQUEST_GPS_SETTINGS = 100
    lateinit var mMap: GoogleMap
    lateinit var routeViewModel: RouteViewModel
    lateinit var nodeListViewModel: NodeListViewModel
    lateinit var route: Route
    lateinit var nodeList: List<MapNode>
    lateinit var dataBinding: ActivityLocationBinding
    private lateinit var cbDataFrameLocal: CBDataFrame
    private lateinit var setGoalButton: Button
    private var mLocTrackingRunning = false
    private var mRouteAlreadySaved = false
    private var trackingStarted = false
    private var sdf: SimpleDateFormat = SimpleDateFormat()
    private var profileId: Int = 0
    private var goalStore: Int = 0
    private lateinit var chronoMeterDuration: Chronometer
    private var timeWhenStopped: Long = 0
    private var timeWhenStopedForStorage: Long = 0
    private var routeCurrentID: Long = 0;

    //TODO move to viewmodel
    private val repository = App.getRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cbDataFrameLocal = CBDataFrame.getInstance()!!
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_location)
        val handler = LocationActivityClickHandler(this)
        val serviceUtil: ServiceUtil = ServiceUtil.newInstance()
        mLocTrackingRunning = serviceUtil.isServiceActive(this)
        trackingStarted = serviceUtil.isServiceActive(this)

        Log.d(ServiceUtil::class.simpleName, "onCreate: $mLocTrackingRunning")
        dataBinding.clickHandler = handler
        dataBinding.locationViewModel = handler

        routeViewModel = ViewModelProviders.of(this).get(RouteViewModel::class.java)
        val routeObserver = androidx.lifecycle.Observer<Route> { newRoute ->
            route = newRoute
            Log.d("Observer", "$newRoute")
            showData(route.length, route.energy, route.avgSpeed)
        }
        routeViewModel.getRouteByIdAsLiveData(routeCurrentID).observe(this, routeObserver)

        nodeListViewModel = ViewModelProviders.of(this).get(NodeListViewModel::class.java)
        nodeListViewModel.getNodes()?.observe(this, androidx.lifecycle.Observer { newList ->
            nodeList = newList
            mMap.clear()
            drawRoute(nodeList)
        })

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setGoalButton = dataBinding.alBtnSetgoal
        setGoalButton.setOnClickListener(View.OnClickListener {
            SharedPref.newInstance().setSentFromFragmentCode(OPENED_FROM_LOCATION_ACT)
            MainActivityClickHandler(supportFragmentManager).onActivitiesClick(it)
        })
    }

    override fun onStart() {
        super.onStart()
        setVisibilities()
        clearData()
        val serviceUtil: ServiceUtil = ServiceUtil.newInstance()
        if (serviceUtil.isServiceActive(this)) {
            continueTracking()
        }
        showData(0.0, 0.0, 0.0)
        setExcercizeType();
        setService()
        dataBinding.alBtnPause.setOnClickListener(View.OnClickListener {
            setServiceStopTracking()
        })
        dataBinding.ibAlReset.setOnClickListener(View.OnClickListener {
            resetServiceTracking()
        })
    }

    private fun continueTracking() {
        trackingInProgressViewChanges()
        val time: Long = nodeListViewModel.getChronometerLastTime()
        val backgroundStartTime: Long = nodeListViewModel.getBackgroundStartTime()
        dataBinding.chrAlDuration.base =
            (SystemClock.elapsedRealtime() - time - (System.currentTimeMillis() - backgroundStartTime));
        dataBinding.chrAlDuration.start()
        nodeListViewModel.continueTracking(this)
        routeViewModel.continueTracking(this)
    }

    private fun resetServiceTracking() {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setMessage(getString(R.string.alert_dialog_message_reset_btn))
        dialog.setPositiveButton(getString(R.string.alert_dialog_positive_reset_save_btn),
            DialogInterface.OnClickListener { _, _ ->
                mMap.clear()
                dataBinding.chrAlDuration.base = SystemClock.elapsedRealtime()
                timeWhenStopped = 0
                clearData();
                if (!mRouteAlreadySaved) {
                    routeViewModel.removeRouteById(routeCurrentID)
                }
                mRouteAlreadySaved = true
                trackingStarted = false
                dataBinding.ibAlReset.isClickable = false
                val drawable2: Drawable? =
                    AppCompatResources.getDrawable(this, R.drawable.ic_light_replay_icon)
                dataBinding.ibAlReset.setImageDrawable(drawable2)
            })
            .setNegativeButton(getString(R.string.alert_dialog_negative_reset_save_btn),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        dialog.show()
    }

    private fun clearData() {
        showData(0.0, 0.0, 0.0)
    }

    private fun setServiceStopTracking() {
        stopTracking();
        dataBinding.ibAlReset.isClickable = true
        val drawable2: Drawable? =
            AppCompatResources.getDrawable(this, R.drawable.ic_light_replay_icon)
        dataBinding.ibAlReset.setImageDrawable(drawable2)
        saveRoute();
    }

    private fun saveRoute() {
        mRouteAlreadySaved = true
        val sharedPref: SharedPref = SharedPref.newInstance()
        if (profileId == Constants.WALK_ID && !sharedPref.doesWalkRouteExist()) {
            sharedPref.setWalkRouteExisting(true)
            Log.d(LocationActivity::class.simpleName, "saveRoute: $profileId") // ok
        } else if (profileId == Constants.RUN_ID && !sharedPref.doesRunRouteExist()) {
            sharedPref.setRunRouteExisting(true)
            Log.d(LocationActivity::class.simpleName, "saveRoute: $profileId")
        } else if (profileId == Constants.RIDE_ID && !sharedPref.doesRideRouteExist()) {
            sharedPref.setRideRouteExisting(true)
            Log.d(LocationActivity::class.simpleName, "saveRoute: $profileId")
        }
    }

    private fun stopTracking() {
        Log.d(LocationActivity::class.simpleName, "$route")
        val intent: Intent = Intent(this, GGLocationService::class.java)
        this.stopService(intent)
        timeWhenStopedForStorage =
            TimeUtils.newInstance().chronometerToMills(dataBinding.chrAlDuration)
        timeWhenStopped = dataBinding.chrAlDuration.base - SystemClock.elapsedRealtime()
        dataBinding.chrAlDuration.stop()
        dataBinding.alBtnStart.visibility = View.VISIBLE
        dataBinding.alBtnPause.visibility = View.GONE
        mLocTrackingRunning = false
    }

    private fun setExcercizeType() {
        val sharedPref: SharedPref = SharedPref.newInstance()
        val i: Int = sharedPref.getClickedTypeShowData2()
        Log.d(LocationActivity::class.simpleName, "setExcercizeType: $i")
        profileId = i
    }

    private fun setService() {
        val sharedPref: SharedPref = SharedPref.newInstance()
        dataBinding.alBtnStart.setOnClickListener(View.OnClickListener {
            val goal: Int = sharedPref.getGoal()
            Log.d(LocationActivity::class.simpleName, "setService: $goal")
            if (goal > 0) {
                startTracking(applicationContext)
            } else {
                Toast.makeText(this, "Set Goal First", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startTracking(context: Context?) {
        if (!trackingStarted) {
            trackingStarted = true
            val datef: Date = Date()
            val date: String = sdf.format(datef)
            val sharedPref: SharedPref = SharedPref.newInstance()
            goalStore = sharedPref.getGoal()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertRoute(
                    Route(0, 0, 0.0, 0.0, date, 0.0, 0.0, profileId, goalStore),
                    this@LocationActivity
                )
            }

        } else {
            startTrackingService(this)
        }
    }

    private fun startTrackingService(context: Context) {
        val intent: Intent = Intent(this, GGLocationService::class.java)
        intent.putExtra(LOC_INTERVAL, Constants.UPDATE_INTERVAL)
        intent.putExtra(LOC_FASTEST_INTERVAL, Constants.FASTEST_INTERVAL)
        intent.putExtra(LOC_DISTANCE, Constants.LOCATION_DISTANCE)
        this.startService(intent)
        trackingInProgressViewChanges();
        mLocTrackingRunning = true
        mRouteAlreadySaved = false
    }

    private fun trackingInProgressViewChanges() {
        runOnUiThread(Runnable {
            chronoMeterDuration = dataBinding.chrAlDuration
            chronoMeterDuration.base = SystemClock.elapsedRealtime() + timeWhenStopped
            chronoMeterDuration.start()
            dataBinding.alBtnStart.visibility = View.GONE
            dataBinding.alBtnPause.visibility = View.VISIBLE
            if (mLocTrackingRunning) {
                val drawable1: Drawable? =
                    AppCompatResources.getDrawable(this, R.drawable.ic_light_save_icon)
                val drawable2: Drawable? =
                    AppCompatResources.getDrawable(this, R.drawable.ic_light_replay_icon)
                dataBinding.ibAlSave.setImageDrawable(drawable1)
                dataBinding.ibAlSave.isClickable = false
                dataBinding.ibAlReset.setImageDrawable(drawable2)
                dataBinding.ibAlReset.isClickable = false
            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        if (((ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission
                    .ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED))
        ) {

            mMap = p0
            mMap.isMyLocationEnabled = true
            mMap.isTrafficEnabled = false
            mMap.isIndoorEnabled = true
            mMap.isBuildingsEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true

            val locationManager = getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!gpsEnabled) {
                val dialog = AlertDialog.Builder(this)
                dialog.setCancelable(false)
                dialog.setTitle(R.string.alert_dialog_title)
                dialog.setMessage(getString(R.string.alert_dialog_message))
                dialog.setPositiveButton(
                    R.string.alert_dialog_positive_button
                ) { _, _ ->
                    val i = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(i, REQUEST_GPS_SETTINGS)
                }

                dialog.setNegativeButton(R.string.alert_dialog_negative_button) { _, _ -> finish() }

                dialog.show()
            }

            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.powerRequirement = Criteria.POWER_LOW
            val bestProvider = locationManager.getBestProvider(criteria, false)
            val location = bestProvider?.let { locationManager.getLastKnownLocation(it) }
            // throws error because of authorization error had to kill this method
            zoomOverCurrentLocation(mMap, location)
        } else {
            finish()
        }
    }


    /**
     * This method is used for zooming over user current location or last known location.
     *
     * @param googleMap google map v2
     */
    private fun zoomOverCurrentLocation(googleMap: GoogleMap, location: Location?) {
        val latLng = location?.let { LatLng(it.latitude, location.longitude) }
        latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15F) }?.let { googleMap.moveCamera(it) }
    }

    /**
     * This method draws a route.
     * @param mRoute list of nodes
     */
    private fun drawRoute(mRoute: List<MapNode>) {
        var drFirstPass = true
        var firstNode: MapNode? = null
        var secondNode: MapNode? = null

        // Redraw the whole route
        val it = mRoute.iterator()
        while (it.hasNext()) {
            if (drFirstPass) {
                secondNode = it.next()
                firstNode = secondNode
                drFirstPass = false
            } else {
                firstNode = secondNode
                secondNode = it.next()
            }
            if (firstNode != null)
                drawSegment(firstNode, secondNode)
        }
    }

    /**
     * This method draws a segment of the route and coloring it in accordance with the speed
     * @param firstNode first point of the rout
     * @param secondNode second point of the rout
     */
    private fun drawSegment(firstNode: MapNode, secondNode: MapNode) {
        if (firstNode.latitude != null && firstNode.longitude != null &&
            secondNode.latitude != null && secondNode.longitude != null
        )
            mMap.addPolyline(
                PolylineOptions().geodesic(true)
                    .add(LatLng(firstNode.latitude!!, firstNode.longitude!!))
                    .add(LatLng(secondNode.latitude!!, secondNode.longitude!!))
                    .width(10f)
                    .color(Color.rgb(0, 255, 0))
            )
    }

    private fun setVisibilities() {
        val sharedPref = SharedPref.newInstance()
        if (!sharedPref.isGoalSet()) {
            dataBinding.alBtnSetgoal.visibility = View.VISIBLE
            dataBinding.ibAlSave.visibility = View.GONE
            dataBinding.ibAlReset.visibility = View.GONE
            dataBinding.alBtnStart.isClickable = false
            dataBinding.chrAlMeters.visibility = View.GONE
            dataBinding.chrAlDuration.visibility = View.GONE
            dataBinding.chrAlKcal.visibility = View.GONE
            dataBinding.chrAlSpeed.visibility = View.GONE
            dataBinding.tvAlKcal.visibility = View.GONE
            dataBinding.tvAlDuration.visibility = View.GONE
            dataBinding.tvAlSpeed.visibility = View.GONE
        } else {
            dataBinding.alBtnSetgoal.visibility = View.GONE
            dataBinding.ibAlSave.visibility = View.INVISIBLE
            dataBinding.ibAlReset.visibility = View.VISIBLE
            dataBinding.alBtnStart.isClickable = true
            dataBinding.chrAlMeters.visibility = View.VISIBLE
            dataBinding.chrAlDuration.visibility = View.VISIBLE
            dataBinding.chrAlKcal.visibility = View.VISIBLE
            dataBinding.chrAlSpeed.visibility = View.VISIBLE
            dataBinding.tvAlKcal.visibility = View.VISIBLE
            dataBinding.tvAlDuration.visibility = View.VISIBLE
            dataBinding.tvAlSpeed.visibility = View.VISIBLE
        }
    }

    private fun showData(distance: Double, kcal: Double, vel: Double) {
        dataBinding.chrAlKcal.text = String.format("%.02f kcal", kcal)
        if (cbDataFrameLocal.measurementSystemId == 1 ||
            cbDataFrameLocal.measurementSystemId == 2
        )
            dataBinding.chrAlMeters.text =
                String.format("%.02f ft", distance * 3.281) // present data in feet
        else
            dataBinding.chrAlMeters.text = String.format("%.02f m", distance)
        dataBinding.chrAlSpeed.text = String.format("%.02f m/s", vel)
    }

    override fun onBackPressed() {
        if (mLocTrackingRunning || !mRouteAlreadySaved) {
            val dialog = AlertDialog.Builder(this)
            dialog.setCancelable(false)
            dialog.setTitle(R.string.alert_dialog_title_back_pressed)
            dialog.setMessage(getString(R.string.alert_dialog_message_back_pressed))
            dialog.setPositiveButton(R.string.alert_dialog_positive_back_pressed) { _, _ ->
                {
                    stopService(Intent(App.appCtxt(), GGLocationService::class.java))
                    //clearCacheData()
                    finish()
                }()
            }
            dialog.setNegativeButton(getString(R.string.alert_dialog_negative_back_pressed)) { _, _ -> }
            dialog.show()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        nodeListViewModel.setChronometerLastTime(
            TimeUtils.newInstance().chronometerToMills(dataBinding.chrAlDuration)
        )
        nodeListViewModel.setBackgroundStartTime(System.currentTimeMillis())
        super.onDestroy()
    }

    override fun onRouteAdded(id: Long) {
        routeCurrentID = id
        runOnUiThread(Runnable {
            nodeListViewModel.setRouteId(routeCurrentID)
            routeViewModel.setRouteID(routeCurrentID)
        })
        Log.d(LocationActivity::class.simpleName, "onRouteAdded: from listener")
        startTrackingService(this)
    }

    companion object {
        const val LOC_INTERVAL: String = "loc_interval"
        const val LOC_FASTEST_INTERVAL: String = "loc_fast_interval"
        const val LOC_DISTANCE: String = "loc_dist"
    }
}