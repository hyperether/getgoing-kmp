package com.hyperether.getgoing_kmp.android.presentation.mock

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
        return emptyList()
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
}