package com.hyperether.getgoing_kmp.android.ui.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dinuscxj.progressbar.CircleProgressBar
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.ui.adapter.formatter.DistanceProgressFormatter
import com.hyperether.getgoing_kmp.android.ui.adapter.formatter.TimeProgressFormatter
import com.hyperether.getgoing_kmp.Constants
import com.hyperether.getgoing_kmp.repository.room.Route

@BindingAdapter("progress_activity_icon")
fun displayActivityProgressIcon(view: View, pAcId: Int) {
    val drawable: Drawable
    view.visibility = View.VISIBLE
    when (pAcId) {
        Constants.ACTIVITY_WALK_ID -> {
            drawable = view.resources.getDrawable(R.drawable.ic_light_walking_icon_white)
            (view as ImageView).setImageDrawable(drawable)
        }

        Constants.ACTIVITY_RUN_ID -> {
            drawable = view.resources.getDrawable(R.drawable.ic_light_running_icon_white)
            (view as ImageView).setImageDrawable(drawable)
        }

        Constants.ACTIVITY_RIDE_ID -> {
            drawable = view.resources.getDrawable(R.drawable.ic_light_bicycling_icon)
            (view as ImageView).setImageDrawable(drawable)
        }

        else -> view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("progress_activity_name")
fun displayActivityProgressName(view: View, pAcId: Int) {
    var acName = ""
    when (pAcId) {
        Constants.ACTIVITY_WALK_ID -> acName = view.resources.getString(R.string.walking)
        Constants.ACTIVITY_RUN_ID -> acName = view.resources.getString(R.string.running)
        Constants.ACTIVITY_RIDE_ID -> acName = view.resources.getString(R.string.cycling)
    }
    (view as TextView).text = acName
}


@BindingAdapter("progress_activity")
fun displayActivityProgress(view: View, lastRoute: Route?) {
    var progress = 0
    var distance = 0.0
    if (lastRoute != null) {
        if (lastRoute.length >= 0) {
            distance = lastRoute.length
        }
        if (lastRoute.goal > 0) {
            progress = (distance / (lastRoute.goal * 100)).toInt()
        } else {
            progress = 100
        }
        val dpf: DistanceProgressFormatter = DistanceProgressFormatter.newInstance(distance)
        (view as CircleProgressBar).setProgressFormatter(dpf)
        (view as CircleProgressBar).progress = progress
    }
}

@BindingAdapter("progress_time")
fun displayTimeProgress(view: View, pDuration: Long) {
    (view as CircleProgressBar).setProgressFormatter(TimeProgressFormatter(pDuration))
}

