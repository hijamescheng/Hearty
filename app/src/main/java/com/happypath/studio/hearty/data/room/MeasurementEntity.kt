package com.happypath.studio.hearty.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.happypath.studio.hearty.domain.BloodPressureMeasurement

@Entity
data class MeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo("created_at") val createdAt: Long,
    @ColumnInfo("systolic") val systolic: Int,
    @ColumnInfo("diastolic") val diastolic: Int,
    @ColumnInfo("pulse") val pulse: Int,
    @ColumnInfo("arm_location") val armLocation: Int,
    @ColumnInfo("note") val note: String,
)

fun BloodPressureMeasurement.toEntity() = MeasurementEntity(
    createdAt = createdAt,
    systolic = systolic,
    diastolic = diastolic,
    pulse = heartRate,
    armLocation = armLocation,
    note = note
)

fun MeasurementEntity.toDomain() = BloodPressureMeasurement(
    createdAt = createdAt,
    systolic = systolic,
    diastolic = diastolic,
    heartRate = pulse,
    armLocation = armLocation,
    note = note
)