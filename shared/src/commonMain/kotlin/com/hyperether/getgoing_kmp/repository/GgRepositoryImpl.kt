package com.hyperether.getgoing_kmp.repository

import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.repository.room.AppDatabase
import com.hyperether.getgoing_kmp.repository.room.Node
import com.hyperether.getgoing_kmp.repository.room.NodeDao
import com.hyperether.getgoing_kmp.repository.room.Route
import com.hyperether.getgoing_kmp.repository.room.RouteAddedCallback
import com.hyperether.getgoing_kmp.repository.room.RouteDao
import com.hyperether.getgoing_kmp.repository.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class GgRepositoryImpl(private val appDatabase: AppDatabase) : GgRepository {

    private var nodeDao: NodeDao
    private val routeDao: RouteDao
    private val userDao: UserDao


    init {
        //todo add instance of database
        nodeDao = appDatabase.nodeDao()
        routeDao = appDatabase.routeDao()
        userDao = appDatabase.userDao()
    }


    override suspend fun daoInsertNode(node: Node) {
        withContext(Dispatchers.IO) {
            nodeDao.insertNode(node)
        }
    }

    override suspend fun getAllNodes(): List<Node> {
        return withContext(Dispatchers.IO) {
            nodeDao.all()
        }
    }

    override suspend fun getAllNodesById(id: Long): List<Node> {
        return withContext(Dispatchers.IO) {
            nodeDao.getAllByRouteId(id)
        }
    }

    override suspend fun insertRoute(route: Route, listener: RouteAddedCallback) {
        withContext(Dispatchers.IO) {
            val routeId = routeDao.insertRoute(route)
            listener.onRouteAdded(routeId)
        }
    }

    override suspend fun updateRoute(route: Route) {
        withContext(Dispatchers.IO) {
            routeDao.updateRoute(route)
        }
    }

    override suspend fun deleteRouteById(id: Long) {
        withContext(Dispatchers.IO) {
            routeDao.deleteRouteById(id)
        }
    }

    override suspend fun getAllRoutes(): List<Route> {
        return withContext(Dispatchers.IO) {
            routeDao.all()
        }
    }

    override suspend fun deleteNodesByRouteId(id: Long) {
        withContext(Dispatchers.IO) {
            nodeDao.deleteAllByRouteId(id)
        }
    }

    override suspend fun insertRouteInit(dbRoute: Route, nodeList: List<Node>) {
        withContext(Dispatchers.IO) {
            val routeId = routeDao.insertRoute(dbRoute)
            nodeList.forEach { currentNode ->
                daoInsertNode(
                    Node(
                        0, currentNode.latitude, currentNode.longitude,
                        currentNode.velocity, currentNode.index, routeId = routeId
                    )
                )
            }
        }
    }

    override suspend fun getRouteById(id: Long): Route? {
        return withContext(Dispatchers.IO) {
            routeDao.getRouteById(id)
        }
    }

    override suspend fun getLastRoute(): Route? {
        return withContext(Dispatchers.IO) {
            routeDao.latestRoute()
        }
    }

    override suspend fun markLastNode() {
        withContext(Dispatchers.IO) {
            val lastNode = nodeDao.lastNode()
            lastNode?.let {
                it.isLast = true
                nodeDao.update(it)
            }
        }
    }

    override suspend fun updateRouteDuration(id: Long, duration: Long) {
        withContext(Dispatchers.IO) {
            val route = routeDao.getRouteById(id)
            route?.let {
                it.duration = duration
                routeDao.updateRoute(it)
            }
        }
    }

    override suspend fun getAllNodesByIdFlow(id: Long): Flow<List<Node>> {
        return nodeDao.getAllNodesByIdFlow(id)
    }

    override suspend fun getRouteByIdFlow(id: Long): Flow<Route> {
        return routeDao.getRouteByIdFlow(id)
    }

    override suspend fun insertUser(user: User): Long {
        val allUsers = userDao.getAllUsers()
        if (allUsers.isEmpty()) {
            return userDao.insertUser(user.toUserEntity())
        }
        return allUsers[0].id
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toUserEntity())
    }

    override suspend fun getUser(userId: Long): Flow<User?> {
        val userEntityFlow = userDao.getUserFlow(userId)

        return userEntityFlow.map { userEntity ->
            userEntity?.toUser()
        }
    }

    override fun getAllUsersFlow(): Flow<List<User>> {
        return userDao.getAllUsersFLow().map {
            it.map { userEntity ->
                userEntity.toUser()
            }
        }
    }
}
