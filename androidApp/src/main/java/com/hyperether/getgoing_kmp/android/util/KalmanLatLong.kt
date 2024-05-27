package com.hyperether.getgoing_kmp.android.util

import java.text.DecimalFormat
import kotlin.math.sqrt

class KalmanLatLong(private val Q_metres_per_second: Float) {
    private val MinAccuracy = 1f

    private var TimeStamp_milliseconds: Long = 0
    private var lat: Double? = null
    private var lng: Double? = null
    private var variance: Float // P matrix. Negative means object uninitialised.

    // NB: units irrelevant, as long as same units used
    // throughout
    init {
        variance = -1f
    }

    fun get_TimeStamp(): Long {
        return TimeStamp_milliseconds
    }

    fun get_lat(): Double {
        return lat!!
    }

    fun get_lng(): Double {
        return lng!!
    }

    fun get_accuracy(): Float {
        return sqrt(variance.toDouble()).toFloat()
    }

    fun SetState(
        lat: Double, lng: Double, accuracy: Float,
        TimeStamp_milliseconds: Long
    ) {
        this.lat = lat
        this.lng = lng
        variance = accuracy * accuracy
        this.TimeStamp_milliseconds = TimeStamp_milliseconds
    }

    // / <summary>
    // / Kalman filter processing for lattitude and longitude
    // / </summary>
    // / <param name="lat_measurement_degrees">new measurement of
    // lattidude</param>
    // / <param name="lng_measurement">new measurement of longitude</param>
    // / <param name="accuracy">measurement of 1 standard deviation error in
    // metres</param>
    // / <param name="TimeStamp_milliseconds">time of measurement</param>
    // / <returns>new state</returns>
    fun Process(
        lat_measurement: Double, lng_measurement: Double,
        accuracy: Float, TimeStamp_milliseconds: Long
    ) {
        var accuracy = accuracy
        if (accuracy < MinAccuracy) accuracy = MinAccuracy
        if (variance < 0) {
            // if variance < 0, object is unitialised, so initialise with
            // current values
            this.TimeStamp_milliseconds = TimeStamp_milliseconds
            lat = lat_measurement
            lng = lng_measurement

            val df = DecimalFormat(
                "#.########"
            ) // limiting output values to 8 decimal places
            lat = df.format(lat).toDouble()
            lng = df.format(lng).toDouble()

            variance = accuracy * accuracy
        } else {
            // else apply Kalman filter methodology

            val TimeInc_milliseconds = (TimeStamp_milliseconds
                    - this.TimeStamp_milliseconds)
            if (TimeInc_milliseconds > 0) {
                // time has moved on, so the uncertainty in the current position
                // increases
                variance += (TimeInc_milliseconds * Q_metres_per_second
                        * Q_metres_per_second) / 1000
                this.TimeStamp_milliseconds = TimeStamp_milliseconds
                // TO DO: USE VELOCITY INFORMATION HERE TO GET A BETTER ESTIMATE
                // OF CURRENT POSITION
            }

            // Kalman gain matrix K = Covarariance * Inverse(Covariance +
            // MeasurementVariance)
            // NB: because K is dimensionless, it doesn't matter that variance
            // has different units to lat and lng
            val K = variance / (variance + accuracy * accuracy)
            // apply K
            lat = lat!! + K * (lat_measurement - lat!!)
            lng = lng!! + K * (lng_measurement - lng!!)

            // limiting output values to 8 decimal places
            val df = DecimalFormat("#.########")

            lat = df.format(lat).replace(",", ".").toDouble()
            lng = df.format(lng).replace(",", ".").toDouble()

            // new Covarariance matrix is (IdentityMatrix - K) * Covarariance
            variance = (1 - K) * variance
        }
    }
}