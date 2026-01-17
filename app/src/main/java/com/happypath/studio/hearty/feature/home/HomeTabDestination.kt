package com.happypath.studio.hearty.feature.home

enum class HomeTabDestination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    Day("day", "Day", "Day"),
    Week("week", "Week", "Week"),
    Month("month", "Month", "Month"),
    Year("year", "Year", "Year")
}