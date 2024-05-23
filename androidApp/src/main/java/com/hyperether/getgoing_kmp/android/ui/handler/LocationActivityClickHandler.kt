package com.hyperether.getgoing_kmp.android.ui.handler

import android.content.Context
import android.view.View
import com.hyperether.getgoing_kmp.android.R
import com.hyperether.getgoing_kmp.android.ui.activity.LocationActivity
import com.hyperether.getgoing_kmp.model.CBDataFrame

class LocationActivityClickHandler(pContext: Context) {
    private val mContext = pContext
    var mTitle: String = when (CBDataFrame.getInstance()?.profileId) {
        1 -> pContext.getString(R.string.activity_walking)
        2 -> pContext.getString(R.string.activity_running)
        3 -> pContext.getString(R.string.activity_cycling)
        else -> ""
    }

    fun onBackPressed(view: View) {
        (LocationActivity::onBackPressed).invoke(mContext as LocationActivity)
    }
}