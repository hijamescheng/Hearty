package com.happypath.studio.hearty.data

import com.happypath.studio.hearty.data.room.MeasurementEntity
import kotlinx.coroutines.flow.Flow

interface LocalMeasurementDataSource {

    fun addMeasurement(entity: MeasurementEntity)

    fun getMeasurements(startDate: Long, endDate: Long): Flow<List<MeasurementEntity>>
}