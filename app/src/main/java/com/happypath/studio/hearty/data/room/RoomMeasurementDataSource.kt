package com.happypath.studio.hearty.data.room

import com.happypath.studio.hearty.data.LocalMeasurementDataSource
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomMeasurementDataSource @Inject constructor(val heartyRoomDB: HeartyRoomDB) :
    LocalMeasurementDataSource {

    override fun addMeasurement(entity: MeasurementEntity) =
        heartyRoomDB.measurementDao().upsert(entity)

    override fun getMeasurements(
        startDate: Long,
        endDate: Long
    ): Flow<List<MeasurementEntity>> {
        return heartyRoomDB.measurementDao().getMeasurements(startDate, endDate)
    }

    override fun getAvgMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<List<MeasurementQueryResult>> =
        heartyRoomDB.measurementDao().getWeeklyAvgMeasurementsBetween(startDate, endDate)

    override fun getAvgMonthlyMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<List<MeasurementQueryResult>> =
        heartyRoomDB.measurementDao().getMonthlyAvgMeasurementsBetween(startDate, endDate)
}