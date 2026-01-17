package com.happypath.studio.hearty.domain

data class BloodPressureMeasurement(
    val createdAt: Long,
    val systolic: Int,
    val diastolic: Int,
    val heartRate: Int,
    val armLocation: Int,
    val note: String
)