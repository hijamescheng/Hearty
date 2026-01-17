package com.happypath.studio.hearty.feature.adddata

import com.happypath.studio.hearty.domain.BloodPressureMeasurement

data class AddDataUIState(
    val createdAt: Long = System.currentTimeMillis(),
    val systolic: Int = 110,
    val diastolic: Int = 75,
    val heartRate: Int = 80,
    val armLocation: Int = ArmLocation.RIGHT.value,
    val note: String = ""
)

fun AddDataUIState.toBloodPressureMeasurement() = BloodPressureMeasurement(
    createdAt = createdAt,
    systolic = systolic,
    diastolic = diastolic,
    heartRate = heartRate,
    armLocation = armLocation,
    note = note
)

enum class ArmLocation(val value: Int) {
    LEFT(0), RIGHT(1)
}