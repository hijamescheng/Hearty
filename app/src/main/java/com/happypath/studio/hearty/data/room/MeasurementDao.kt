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
            "    (strftime('%s', date(created_at / 1000, 'unixepoch', 'localtime')) * 1000) AS startDate, " +
            "    (strftime('%s', date(created_at / 1000, 'unixepoch', 'localtime')) * 1000) + (24 * 60 * 60 * 1000) - 1 AS endDate, " +
            "    AVG(systolic)   AS avg_sys, " +
            "    AVG(diastolic)   AS avg_dia, " +
            "    AVG(pulse) AS avg_pulse " +
            "FROM MeasurementEntity " +
            "WHERE created_at BETWEEN :startDate AND  :endDate " +
            "GROUP BY startDate " +
            "ORDER BY startDate DESC")
    fun getWeeklyAvgMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    @Query( "SELECT" +
            "    startDate, " +
            "    startDate + (7 * 24 * 60 * 60 * 1000) - 1 AS endDate," +
            "    AVG(systolic)  AS avg_sys," +
            "    AVG(diastolic) AS avg_dia," +
            "    AVG(pulse)     AS avg_pulse" +
            " FROM (" +
            "    SELECT" +
            "        ((created_at / 1000 - strftime('%s', '1970-01-05')) / (7 * 86400))" +
            "            * (7 * 86400) * 1000" +
            "            + strftime('%s', '1970-01-05') * 1000" +
            "            AS startDate," +
            "        systolic," +
            "        diastolic," +
            "        pulse" +
            "    FROM MeasurementEntity " +
            "    WHERE created_at BETWEEN :startDate AND :endDate" +
            ") " +
            "GROUP BY startDate " +
            "ORDER BY startDate DESC")
    fun getMonthlyAvgMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    @Query("SELECT " +
            "    (strftime('%s', date(created_at / 1000, 'unixepoch', 'localtime', 'start of month')) * 1000) " +
            "        AS startDate," +
            "    (strftime('%s', date(created_at / 1000, 'unixepoch', 'localtime', 'start of month', '+1 month')) * 1000) " +
            "        - 1 AS endDate," +
            "    AVG(systolic)  AS avg_sys," +
            "    AVG(diastolic) AS avg_dia," +
            "    AVG(pulse)     AS avg_pulse" +
            " FROM MeasurementEntity " +
            "WHERE created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY startDate " +
            "ORDER BY startDate ASC")
    fun getYearlyAvgMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    @Query("SELECT " +
            "    :startDate AS startDate," +
            "    :endDate   AS endDate," +
            "    AVG(systolic)  AS avg_sys," +
            "    AVG(diastolic) AS avg_dia," +
            "    AVG(pulse)     AS avg_pulse " +
            "FROM MeasurementEntity " +
            "WHERE created_at BETWEEN :startDate AND :endDate")
    fun getDailyAvgMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(entity: MeasurementEntity)
}
