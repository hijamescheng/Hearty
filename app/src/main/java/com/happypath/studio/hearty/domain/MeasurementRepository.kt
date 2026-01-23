package com.happypath.studio.hearty.domain

import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {
    suspend fun addMeasurement(measurement: BloodPressureMeasurement): Result<Unit>
    suspend fun getAvgDailyMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>>

    suspend fun getAvgWeeklyMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>>

    suspend fun getAvgMonthlyMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>>

    suspend fun getAvgYearlyMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>>
}