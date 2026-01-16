package com.happypath.studio.hearty.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String,
    val isBottomNav: Boolean = true
) {
    HOME("home", "Home", Icons.Default.Home, Icons.Outlined.Home, "Home"),
    Journal("journal", "Journal", Icons.Default.DateRange, Icons.Outlined.DateRange, "Journal"),
    Profile("profile", "Profile", Icons.Default.Person, Icons.Outlined.Person, "Profile"),
    AddData("add_data", "Add data", Icons.Default.Add, Icons.Outlined.Add, "Add data", false)
}