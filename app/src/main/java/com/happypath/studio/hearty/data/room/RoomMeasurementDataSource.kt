package com.happypath.studio.hearty.data.room

import com.happypath.studio.hearty.data.LocalMeasurementDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomMeasurementDataSource @Inject constructor(val heartyRoomDB: HeartyRoomDB) :
    LocalMeasurementDataSource {

    override fun addMeasurement(entity: MeasurementEntity) {
        heartyRoomDB.measurementDao().upsert(entity)
    }

    override fun getMeasurements(
        startDate: Long,
        endDate: Long
    ): Flow<List<MeasurementEntity>> {
        return heartyRoomDB.measurementDao().getMeasurements(startDate, endDate)
    }
}