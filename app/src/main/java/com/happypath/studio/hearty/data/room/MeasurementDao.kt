package com.happypath.studio.hearty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measuremententity WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    fun getMeasurements(startDate: Long, endDate: Long): Flow<List<MeasurementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entity: MeasurementEntity)
}
