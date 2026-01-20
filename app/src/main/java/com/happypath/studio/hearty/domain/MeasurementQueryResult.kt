package com.happypath.studio.hearty.domain

data class MeasurementQueryResult (
    val date: Long,
    val avg_sys: Long,
    val avg_dia: Long,
    val avg_pulse: Long
)