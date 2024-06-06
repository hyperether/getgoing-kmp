package com.hyperether.getgoing_kmp.android.presentation.mock

import com.hyperether.getgoing_kmp.model.CurrentTracking
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.model.UserGender
import com.hyperether.getgoing_kmp.repository.GgRepository
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class MockRepo : GgRepository {
    override suspend fun daoInsertNode(node: Node) {

    }

    override suspend fun getAllNodes(): List<Node> {
        return listOf()
    }

    override suspend fun getAllNodesById(id: Long): List<Node> {
        return listOf()
    }

    override suspend fun insertRoute(route: Route, listener: RouteAddedCallback) {

    }

    override suspend fun updateRoute(route: Route) {

    }

    override suspend fun deleteRouteById(id: Long) {

    }

    override suspend fun getAllRoutes(): List<Route> {
        return listOf(
            Route(0, 350, 400.0, 1000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(0, 350, 400.0, 2000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 3000),
            Route(0, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(0, 350, 400.0, 4000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 5000),
            Route(0, 350, 400.0, 3000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(0, 350, 400.0, 9000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 3000),
            Route(0, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(0, 350, 400.0, 5000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 5000)
        )
    }

    fun getAllRoutesMock(): List<Route> {
        return listOf(
            Route(0, 350, 400.0, 1000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(1, 350, 400.0, 2000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 500),
            Route(2, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(3, 350, 400.0, 4000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 1000),
            Route(4, 350, 400.0, 3000.0, "05.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(5, 350, 400.0, 9000.0, "06.04.2024. 09:26:45", 0.0, 0.0, 0, 3500),
            Route(6, 350, 400.0, 3000.0, "07.04.2024. 09:26:45", 0.0, 0.0, 0, 2000),
            Route(7, 350, 400.0, 5000.0, "08.04.2024. 09:26:45", 0.0, 0.0, 0, 5000)
        )
    }

    override suspend fun deleteNodesByRouteId(id: Long) {

    }

    override suspend fun insertRouteInit(dbRoute: Route, nodeList: List<Node>) {

    }

    override suspend fun getRouteById(id: Long): Route? {
        return Route(1, 1, 1.0, 1.0, "", 1.0, 1.0, 1, 1)
    }

    override suspend fun getLastRoute(): Route? {

        return Route(1, 1, 1.0, 1.0, "", 1.0, 1.0, 1, 1)
    }

    override suspend fun markLastNode() {

    }

    override suspend fun updateRouteDuration(id: Long, duration: Long) {

    }

    override suspend fun getAllNodesByIdFlow(id: Long): Flow<List<Node>> {

        return MutableStateFlow(listOf())
    }

    override suspend fun getRouteByIdFlow(id: Long): Flow<Route> {
        return MutableStateFlow(Route(0, 0, 0.0, 0.0, "", 0.0, 0.0, 0, 2000))
    }

    override suspend fun insertUser(user: User): Long {
        return 0L
    }

    override suspend fun updateUser(user: User) {

    }

    override suspend fun getUser(userId: Long): Flow<User?> {
        return MutableStateFlow(
            User(
                id = 0,
                gender = UserGender.Male,
                age = 1,
                height = 110,
                weight = 40,
                totalKm = 0.00,
                totalKcal = 0
            )
        )
    }

    override fun getAllUsersFlow(): Flow<List<User>> {
        return MutableStateFlow(listOf())
    }

    override fun initCurrentTracking(id: Long) {

    }

    override fun updateCurrentTrackingTime(time: Long) {

    }

    override fun updateCurrentTrackingExercise(exercise: Int) {

    }

    override fun getCurrentTracking(): CurrentTracking {
        return CurrentTracking()
    }

    override fun updateCurrentTrackingDistance(distance: Double) {

    }
}