package com.happypath.studio.hearty.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

fun Long.toDate(pattern: String = "MMM dd, yyyy 'at' HH:mm a"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}