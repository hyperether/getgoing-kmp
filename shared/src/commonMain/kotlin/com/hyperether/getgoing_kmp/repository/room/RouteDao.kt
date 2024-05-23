package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("SELECT * from routes")
    fun getAll(): Flow<List<Route>>

    @Insert
    suspend fun insertRoute(route: Route): Long

    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getRouteById(id: Long): Route

    @Query("SELECT * FROM routes WHERE id = :id")
    fun getRouteByIdAsLiveData(id: Long): Flow<Route>?

    @Delete
    suspend fun deleteRoutes(vararg routes: Route)

    @Query("DELETE FROM routes WHERE id = :id")
    suspend fun deleteRouteById(id: Long)

    @Query("SELECT * from routes ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): Route

    @Update
    suspend fun updateRoute(route: Route)

    @Query("SELECT * FROM routes WHERE goal > 0 ORDER BY id DESC LIMIT 1")
    suspend fun getLatestRoute(): Route?
}