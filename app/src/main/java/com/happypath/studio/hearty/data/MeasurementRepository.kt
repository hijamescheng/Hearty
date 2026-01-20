package com.happypath.studio.hearty.data

import androidx.sqlite.SQLiteException
import com.happypath.studio.hearty.data.room.toDomain
import com.happypath.studio.hearty.data.room.toEntity
import com.happypath.studio.hearty.domain.BloodPressureMeasurement
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import com.happypath.studio.hearty.domain.MeasurementRepository
import jakarta.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MeasurementRepositoryImpl @Inject constructor(
    val localDataSource: LocalMeasurementDataSource,
    @Named("IODispatcher") val ioDispatcher: CoroutineDispatcher
) :
    MeasurementRepository {

    override suspend fun addMeasurement(measurement: BloodPressureMeasurement): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                localDataSource.addMeasurement(measurement.toEntity())
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }
        }

    override suspend fun getMeasurements(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<BloodPressureMeasurement>>> {
        return localDataSource.getMeasurements(startDate, endDate).map { result ->
            Result.success(result.map { it.toDomain() })
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAvgMeasurementsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>> {
        return localDataSource.getAvgMeasurementsBetween(startDate, endDate).map { result ->
            Result.success(result)
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(ioDispatcher)
    }
}