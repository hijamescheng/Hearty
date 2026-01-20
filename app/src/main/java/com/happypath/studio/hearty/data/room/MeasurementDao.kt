package com.happypath.studio.hearty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measuremententity WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    fun getMeasurements(startDate: Long, endDate: Long): Flow<List<MeasurementEntity>>

    @Query("SELECT " +
            "    (strftime('%s', date(created_at / 1000, 'unixepoch', 'localtime')) * 1000) AS date, " +
            "    AVG(systolic)   AS avg_sys, " +
            "    AVG(diastolic)   AS avg_dia, " +
            "    AVG(pulse) AS avg_pulse " +
            "FROM MeasurementEntity " +
            "WHERE created_at BETWEEN :startDate\t AND  :endDate " +
            "GROUP BY date " +
            "ORDER BY date DESC")
    fun getAvgMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entity: MeasurementEntity)
}
