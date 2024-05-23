package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {
    @Query("SELECT * from nodes")
    fun getAll(): Flow<List<MapNode>>

    @Query("SELECT * FROM nodes WHERE routeId = :id")
    fun getAllByRouteId(id: Long): Flow<List<MapNode>>

    @Insert
    fun insert(mapNode: MapNode)

    @Delete
    fun delete(mapNode: MapNode)

    @Delete
    fun deleteNodes(vararg mapNodes: MapNode)

    @Query("SELECT * FROM nodes WHERE routeId = :id")
    fun getAllByRouteIdAsLiveData(id: Long): Flow<List<MapNode>>

    @Query("DELETE FROM nodes WHERE routeId = :id")
    fun deleteAllByRouteId(id: Long)

    @Insert
    fun insertNode(node: MapNode)
}