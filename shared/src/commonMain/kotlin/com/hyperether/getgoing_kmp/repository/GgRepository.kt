package com.hyperether.getgoing_kmp.repository

import com.hyperether.getgoing_kmp.model.CurrentTracking
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import kotlinx.coroutines.flow.Flow

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
    suspend fun getAllNodesByIdFlow(id: Long): Flow<List<Node>>
    suspend fun getRouteByIdFlow(id: Long): Flow<Route>
    suspend fun insertUser(user: User):Long
    suspend fun updateUser(user: User)
    suspend fun getUser(userId: Long): Flow<User?>
    fun getAllUsersFlow(): Flow<List<User>>
    fun initCurrentTracking(id: Long)
    fun updateCurrentTrackingTime(time: Long)
    fun updateCurrentTrackingExercise(exercise: Int)
    fun getCurrentTracking(): CurrentTracking
    fun updateCurrentTrackingDistance(distance: Double)
}