package com.hyperether.getgoing_kmp.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE id = :id LIMIT 1")
    fun getUserFlow(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM UserEntity")
    fun getAllUsersFLow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>
}