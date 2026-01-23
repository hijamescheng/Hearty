package com.happypath.studio.hearty.data

import com.happypath.studio.hearty.data.room.MeasurementEntity
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import kotlinx.coroutines.flow.Flow

interface LocalMeasurementDataSource {

    fun addMeasurement(entity: MeasurementEntity)

    fun getAvgDailyMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    fun getAvgWeeklyMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    fun getAvgMonthlyMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>

    fun getAvgYearlyMeasurementsBetween(startDate: Long, endDate: Long): Flow<List<MeasurementQueryResult>>
}