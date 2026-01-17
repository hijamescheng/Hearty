package com.happypath.studio.hearty.util

import java.time.LocalDate
import java.time.ZoneId

fun getStartAndEndOfToday(): Pair<Long, Long> {
    val today = LocalDate.now()

    // Start of day
    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    // End of day (23:59:59.999)
    val endOfDay = today
        .atTime(23, 59, 59, 999_000_000)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    return startOfDay to endOfDay
}