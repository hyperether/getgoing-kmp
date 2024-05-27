package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RouteDao {
    @Query("SELECT * FROM Route")
    suspend fun all(): List<Route>

    @Insert
    suspend fun insertRoute(route: Route): Long

    @Query("SELECT * FROM Route WHERE id = :id")
    suspend fun getRouteById(id: Long): Route?

    @Delete
    suspend fun deleteRoutes(vararg routes: Route)

    @Query("DELETE FROM Route WHERE id = :id")
    suspend fun deleteRouteById(id: Long)

    @Query("SELECT * FROM Route WHERE goal > 0 ORDER BY id DESC LIMIT 1")
    suspend fun latestRoute(): Route?

    @Update
    suspend fun updateRoute(route: Route)
}
