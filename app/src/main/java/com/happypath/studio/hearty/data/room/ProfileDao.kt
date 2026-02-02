package com.happypath.studio.hearty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ProfileEntity LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    @Query("DELETE FROM ProfileEntity")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProfileEntity)

    @Transaction
    suspend fun deleteAndInsertTransaction(entity: ProfileEntity) {
        deleteAll()
        upsert(entity)
    }
}