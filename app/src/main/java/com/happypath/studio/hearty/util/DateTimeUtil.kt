package com.happypath.studio.hearty.util

import com.happypath.studio.hearty.feature.home.MeasurementScope
import com.happypath.studio.hearty.feature.home.MeasurementScope.DAY
import com.happypath.studio.hearty.feature.home.MeasurementScope.WEEK
import com.happypath.studio.hearty.feature.home.MeasurementScope.MONTH
import com.happypath.studio.hearty.feature.home.MeasurementScope.YEAR
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

fun getStartDateOf(scope: MeasurementScope): Long {
    val zoneId: ZoneId = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zoneId)
    return when (scope) {
        DAY -> LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        WEEK -> now
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toLocalDate()
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        MONTH -> now
            .with(TemporalAdjusters.firstDayOfMonth())
            .toLocalDate()
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        YEAR -> now
            .with(TemporalAdjusters.firstDayOfYear())
            .toLocalDate()
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()
    }
}

fun previousDayRange(startDateMillis: Long, isPrevious: Boolean): Pair<Long, Long> {
    val zone = ZoneId.systemDefault()

    val startOfPreviousDay = Instant.ofEpochMilli(startDateMillis)
        .atZone(zone)
        .toLocalDate()
        .let {
            if (isPrevious) it.minusDays(1) else it.plusDays(1)
        }
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()

    val endOfPreviousDay = Instant.ofEpochMilli(startDateMillis)
        .atZone(zone)
        .toLocalDate()
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()

    return startOfPreviousDay to endOfPreviousDay
}

fun getStartAndEndOfToday(
    dateRange: DateRange = DateRange.CURRENT,
    targetDate: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): Pair<Long, Long> {
    val today = Instant
        .ofEpochMilli(targetDate)
        .atZone(zoneId)
        .toLocalDate()

    // Start of day
    val startOfDay = today
        .atStartOfDay(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusDays(1)
                DateRange.NEXT -> it.plusDays(1)
                else -> it
            }
        }
        .toInstant().toEpochMilli()

    // End of day (23:59:59.999)
    val endOfDay = today
        .atTime(23, 59, 59, 999_000_000)
        .atZone(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusDays(1)
                DateRange.NEXT -> it.plusDays(1)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    return startOfDay to endOfDay
}

fun getCurrentWeekRange(
    dateRange: DateRange = DateRange.CURRENT,
    targetDate: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): Pair<Long, Long> {
    val now = Instant.ofEpochMilli(targetDate)
        .atZone(ZoneId.systemDefault())

    val startOfWeek = now
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusDays(7)
                DateRange.NEXT -> it.plusDays(7)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    val endOfWeek = now
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        .toLocalDate()
        .atTime(LocalTime.MAX)
        .atZone(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusWeeks(1)
                DateRange.NEXT -> it.plusWeeks(1)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    return startOfWeek to endOfWeek
}

fun getCurrentMonthRange(
    dateRange: DateRange = DateRange.CURRENT,
    targetDate: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): Pair<Long, Long> {

    val now = Instant.ofEpochMilli(targetDate)
        .atZone(ZoneId.systemDefault())

    val startOfMonth = now
        .with(TemporalAdjusters.firstDayOfMonth())
        .toLocalDate()
        .atStartOfDay(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusMonths(1)
                DateRange.NEXT -> it.plusMonths(1)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    val endOfMonth = now
        .with(TemporalAdjusters.lastDayOfMonth())
        .toLocalDate()
        .atTime(LocalTime.MAX)
        .atZone(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusMonths(1)
                DateRange.NEXT -> it.plusMonths(1)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    return startOfMonth to endOfMonth
}

fun getCurrentYearRange(
    dateRange: DateRange = DateRange.CURRENT,
    targetDate: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): Pair<Long, Long> {

    val now = Instant.ofEpochMilli(targetDate)
        .atZone(ZoneId.systemDefault())

    val start = now
        .with(TemporalAdjusters.firstDayOfYear())
        .toLocalDate()
        .atStartOfDay(zoneId)
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusYears(1)
                DateRange.NEXT -> it.plusYears(1)
                else -> it
            }
        }
        .toInstant()
        .toEpochMilli()

    val endExclusive = now
        .with(TemporalAdjusters.firstDayOfNextYear())
        .toLocalDate()
        .let {
            when (dateRange) {
                DateRange.PREVIOUS -> it.minusYears(1)
                DateRange.NEXT -> it.plusYears(1)
                else -> it
            }
        }
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    return start to endExclusive
}

fun isCurrentDateWithin(startDate: Long, endDate: Long): Boolean {
    val now = Instant.ofEpochMilli(System.currentTimeMillis())
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return now >= startDate && now <= endDate
}

fun Long.toDateString(pattern: String = "MMM dd, yyyy 'at' HH:mm a"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun formatDateWithTodayYesterday(
    epochMillis: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val date = Instant.ofEpochMilli(epochMillis)
        .atZone(zoneId)
        .toLocalDate()

    val today = LocalDate.now(zoneId)

    return when (date) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            date.format(formatter)
        }
    }
}

fun formatTime(
    epochMillis: Long = System.currentTimeMillis(),
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(epochMillis)
        .atZone(zoneId)
        .format(formatter)
}

fun updateDateTimeWithHourMinute(
    epochMillis: Long,
    hour: Int,
    minute: Int,
    zoneId: ZoneId = ZoneId.systemDefault()
): Long {
    val dateTime = Instant.ofEpochMilli(epochMillis).atZone(zoneId)
    val updated = dateTime
        .withHour(hour)
        .withMinute(minute)
        .withSecond(0)
        .withNano(0)
    return updated.toInstant().toEpochMilli()
}

enum class DateRange(val value: Int) {
    PREVIOUS(0), CURRENT(1), NEXT(2)
}