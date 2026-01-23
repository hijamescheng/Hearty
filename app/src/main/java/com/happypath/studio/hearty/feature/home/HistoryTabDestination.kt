package com.happypath.studio.hearty.feature.home

enum class HistoryTabDestination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    Week("week", "Week", "Week"),
    Month("month", "Month", "Month"),
    Year("year", "Year", "Year")
}