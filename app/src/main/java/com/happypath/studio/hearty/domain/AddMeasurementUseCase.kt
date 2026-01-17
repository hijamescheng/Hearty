package com.happypath.studio.hearty.domain

import javax.inject.Inject

class AddMeasurementUseCase @Inject constructor(private val repository: MeasurementRepository) {
    suspend operator fun invoke(measurement: BloodPressureMeasurement) {
        repository.addMeasurement(measurement)
    }
}