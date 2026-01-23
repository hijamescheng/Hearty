package com.happypath.studio.hearty.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvgDailyMeasurementUseCase @Inject constructor(private val repository: MeasurementRepository) {
    suspend operator fun invoke(
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<MeasurementQueryResult>>> = repository.getAvgDailyMeasurementsBetween(startDate, endDate)
}