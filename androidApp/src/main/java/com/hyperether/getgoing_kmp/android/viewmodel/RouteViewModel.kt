package com.hyperether.getgoing_kmp.android.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.MapNode
import com.hyperether.getgoing_kmp.repository.room.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {
    private val repository = App.getRepository()
    private lateinit var routeList: LiveData<List<Route>>
    private var routeId: MutableLiveData<Long> = MutableLiveData()

    //    private var route: LiveData<Route> = Transformations.switchMap(
//        routeId,
//        Function { input -> GgRepository.getRouteByIdAsLiveData(input) })
    private var route: LiveData<Route> =
        routeId.switchMap { input -> repository.getRouteByIdAsLiveData(input)?.asLiveData() }


    val currentRoute: MutableLiveData<Route> by lazy {
        MutableLiveData<Route>()
    }

    suspend fun getLatestRoute(): LiveData<Route>? {
        return repository.getLastRoute()?.asLiveData()
    }

    fun getRouteByIdAsLiveData(id: Long): LiveData<Route> {
        return route
    }

    fun setRouteID(id: Long) {
        routeId.setValue(id)
    }

    fun getAllRoutes(): LiveData<List<Route>> {
        routeList = repository.getAllRoutes().asLiveData()
        return routeList
    }

    fun getNodeListById(id: Long): LiveData<List<MapNode>> {
        return repository.getAllNodesById(id).asLiveData()
    }

    fun removeRouteById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNodesByRouteId(id)
            repository.deleteRouteById(id)
        }
    }

    fun continueTracking(activity: Activity) {
        App.getHandler().post(Runnable {
            CoroutineScope(Dispatchers.IO).launch {
                val id: Long = repository.getLastRoute2()!!.id
                activity.runOnUiThread(Runnable {
                    setRouteId(id)
                    getNodesById(id)
                    getRouteByIdAsLiveData(id)
                })
            }

        })
    }

    private fun getNodesById(id: Long): LiveData<List<MapNode>> {
        return repository.getAllNodesById(id).asLiveData()
    }

    private fun setRouteId(id: Long) {
        routeId.value = id

    }
}