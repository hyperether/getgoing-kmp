package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Created by Slobodan on 7/11/2017.
 */
@Dao
interface NodeDao {
    @Query("SELECT * FROM Node")
    suspend fun all(): List<Node>

    @Query("SELECT * FROM Node WHERE routeId = :id")
    suspend fun getAllByRouteId(id: Long): List<Node>

    @Insert
    suspend fun insertNode(node: Node)

    @Query("SELECT * FROM Node ORDER BY id DESC LIMIT 1")
    suspend fun lastNode(): Node?

    @Query("DELETE FROM Node WHERE routeId = :id")
    suspend fun deleteAllByRouteId(id: Long)

    @Update
    suspend fun update(node: Node)
}
