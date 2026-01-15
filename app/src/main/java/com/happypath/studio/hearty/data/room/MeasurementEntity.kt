package com.happypath.studio.hearty.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeasurementEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("created_at") val createdAt: Long,
    @ColumnInfo("systolic") val systolic: Int,
    @ColumnInfo("diastolic") val diastolic: Int,
    @ColumnInfo("pulse") val pulse: Int,
    @ColumnInfo("arm_location") val armLocation: Int,
    @ColumnInfo("note") val note: String,
)