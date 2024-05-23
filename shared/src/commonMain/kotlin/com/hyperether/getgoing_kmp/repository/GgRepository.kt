package com.hyperether.getgoing_kmp.repository

import com.hyperether.getgoing_kmp.repository.callback.ZeroNodeInsertCallback
import com.hyperether.getgoing_kmp.repository.room.AppDatabase
import com.hyperether.getgoing_kmp.repository.room.MapNode
import com.hyperether.getgoing_kmp.repository.room.NodeDao
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import com.hyperether.getgoing_kmp.repository.room.RouteDao
import kotlinx.atomicfu.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class GgRepository(private val appDatabase: AppDatabase) {

    private var nodeDao: NodeDao
    private val routeDao: RouteDao
    private lateinit var allNodes: Flow<List<MapNode>>
    private lateinit var allNodesById: Flow<List<MapNode>>
    private var nodeListLiveData: Flow<List<MapNode>>? = null


    init {
        //todo add instance of database
        nodeDao = appDatabase.nodeDao()
        routeDao = appDatabase.routeDao()
    }

    fun insert(mapNode: MapNode) {
        nodeDao.insert(mapNode)
    }


    fun daoInsertNode(node: MapNode) {
        nodeDao.insertNode(node)
    }

    suspend fun insertRoute(route: Route, listener: RouteAddedCallback) {
        var routeId = 0L
        routeId = routeDao.insertRoute(route)
        listener.onRouteAdded(routeId)
    }

    fun getNodesLiveData(): Flow<List<MapNode>>? {
        return nodeListLiveData
    }

    suspend fun getLastRoute(): Flow<Route>? {
        val data: MutableStateFlow<Route>? = null

        data?.value = routeDao.getLast()

        return data
    }

    suspend fun getLastRoute2(): Route? {
        return routeDao.getLatestRoute()
    }

    suspend fun updateRoute(route: Route) {
        routeDao.updateRoute(route)
    }

    fun getNodesById(id: Long): Flow<List<MapNode>> {
        return nodeDao.getAllByRouteId(id)
    }

    suspend fun insertRouteInit(
        dbRoute: Route?,
        nodeList: List<MapNode>,
        callback: ZeroNodeInsertCallback
    ) {
        val routeId: Long = routeDao.insertRoute(dbRoute!!)
        val route: Flow<Route?>? = routeDao.getRouteByIdAsLiveData(routeId)

        if (route != null) {
            for (currentNode in nodeList) {
                nodeDao.insert(
                    MapNode(
                        0, currentNode.latitude, currentNode.longitude,
                        currentNode.velocity, currentNode.number,
                        routeId
                    )
                )
            }

            callback.onAdded()
        }

    }

    suspend fun insertRouteInitMainActivity(route: Route, nodeList: List<MapNode>) {
        var routeId: Long
        routeId = routeDao.insertRoute(route)
        var route: Flow<Route?>? = routeDao.getRouteByIdAsLiveData(routeId)
        if (route != null) {
            for (item in nodeList) {
                daoInsertNode(
                    MapNode(
                        0, item.latitude, item.longitude, item.velocity, item.number, routeId
                    )
                )
            }
        }
    }

    fun getRouteByIdAsLiveData(id: Long): Flow<Route>? {
        return routeDao.getRouteByIdAsLiveData(id)
    }

    fun getAllRoutes(): Flow<List<Route>> {
        return routeDao.getAll()
    }


    fun getAllNodesById(id: Long): Flow<List<MapNode>> {
        allNodesById = nodeDao.getAllByRouteIdAsLiveData(id)
        return allNodesById
    }


    suspend fun deleteNodesByRouteId(id: Long) {
        nodeDao.deleteAllByRouteId(id)
    }

    suspend fun deleteRouteById(id: Long) {
        routeDao.deleteRouteById(id)
    }

}