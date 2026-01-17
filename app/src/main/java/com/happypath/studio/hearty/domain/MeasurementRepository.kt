package com.happypath.studio.hearty.domain

import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {
    suspend fun addMeasurement(measurement: BloodPressureMeasurement): Result<Unit>
    suspend fun getMeasurements(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<BloodPressureMeasurement>>>
}