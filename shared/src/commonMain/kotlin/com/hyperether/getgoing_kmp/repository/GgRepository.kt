package com.hyperether.getgoing_kmp.repository

import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback

interface GgRepository {
    suspend fun daoInsertNode(node: Node)
    suspend fun getAllNodes(): List<Node>
    suspend fun getAllNodesById(id: Long): List<Node>
    suspend fun insertRoute(route: Route, listener: RouteAddedCallback)
    suspend fun updateRoute(route: Route)
    suspend fun deleteRouteById(id: Long)
    suspend fun getAllRoutes(): List<Route>
    suspend fun deleteNodesByRouteId(id: Long)
    suspend fun insertRouteInit(dbRoute: Route, nodeList: List<Node>)
    suspend fun getRouteById(id: Long): Route?
    suspend fun getLastRoute(): Route?
    suspend fun markLastNode()
    suspend fun updateRouteDuration(id: Long, duration: Long)
}