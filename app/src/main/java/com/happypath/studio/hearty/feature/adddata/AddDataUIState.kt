package com.happypath.studio.hearty.feature.adddata

import com.happypath.studio.hearty.domain.BloodPressureMeasurement
import com.happypath.studio.hearty.util.formatDateWithTodayYesterday
import com.happypath.studio.hearty.util.formatTime

data class AddDataUIState(
    val createdAt: Long = System.currentTimeMillis(),
    val systolic: Int = 110,
    val diastolic: Int = 75,
    val heartRate: Int = 80,
    val armLocation: Int = ArmLocation.RIGHT.value,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val dateString: String = formatDateWithTodayYesterday(),
    val timeString: String = formatTime(),
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