package com.hyperether.getgoing_kmp.android.ui.handler

import android.view.View
import androidx.fragment.app.FragmentManager
import com.hyperether.getgoing_kmp.android.ui.fragment.ActivitiesFragment
import com.hyperether.getgoing_kmp.android.ui.fragment.ProfileFragment

class MainActivityClickHandler(pManager: FragmentManager) {
    private val mManager = pManager
    fun onProfileClick(view: View) {
        val profileFragment = ProfileFragment()
        profileFragment.show(mManager, "ProfileFragment")
    }

    fun onActivitiesClick(view: View) {
        val activitiesFragment = ActivitiesFragment()
        activitiesFragment.show(mManager, "ActivitiesFragment")
    }
}