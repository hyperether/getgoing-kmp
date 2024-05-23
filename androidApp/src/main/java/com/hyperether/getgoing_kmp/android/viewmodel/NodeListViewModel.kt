package com.hyperether.getgoing_kmp.android.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.android.SharedPref
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.MapNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NodeListViewModel : ViewModel() {

    private val repository = App.getRepository()

    private val routeID = MutableLiveData<Long>()
    private val nodesByRouteId: LiveData<List<MapNode>> =
        routeID.switchMap { input -> repository.getAllNodesById(input).asLiveData() }

    val currentNodeList: LiveData<List<MapNode>> by lazy {
        MutableLiveData<List<MapNode>>()
    }

    fun getNodes(): LiveData<List<MapNode>>? {
        return repository.getNodesLiveData()?.asLiveData()
    }

    fun getNodesById(id: Long): LiveData<List<MapNode>> {
        return repository.getNodesById(id).asLiveData()
    }

    fun setChronometerLastTime(time: Long) {
        SharedPref.newInstance().setLastTime(time)
    }

    fun getBackgroundStartTime(): Long {
        return SharedPref.newInstance().getBackgroundStartTime()
    }

    fun setBackgroundStartTime(currentTimeMillis: Long) {
        SharedPref.newInstance().setBackgroundStartTime(currentTimeMillis)
    }

    fun getChronometerLastTime(): Long {
        return SharedPref.newInstance().getLastTime()
    }

    fun setRouteId(id: Long) {
        routeID.value = id
    }

    fun continueTracking(activity: Activity) {
        App.getHandler().post(Runnable {
            CoroutineScope(Dispatchers.IO).launch {
                val id: Long = repository.getLastRoute2()!!.id
                activity.runOnUiThread(Runnable {
                    setRouteId(id)
                    getNodesById(id)
                })
            }
        })
    }
}