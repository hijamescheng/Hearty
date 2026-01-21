package com.happypath.studio.hearty.domain

data class MeasurementQueryResult (
    val startDate: Long,
    val endDate: Long,
    val avg_sys: Long,
    val avg_dia: Long,
    val avg_pulse: Long
)