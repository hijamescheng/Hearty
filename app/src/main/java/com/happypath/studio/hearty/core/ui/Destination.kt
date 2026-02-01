package com.happypath.studio.hearty.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
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
    JOURNAL("journal", "Journal", Icons.Default.DateRange, Icons.Outlined.DateRange, "Journal"),
    PROFILE("profile", "Profile", Icons.Default.Person, Icons.Outlined.Person, "Profile"),
    EDIT_PROFILE("edit_profile", "Add data", Icons.Default.Edit, Icons.Outlined.Edit, "Edit profile", false),
    ADD_DATA("add_data", "Add data", Icons.Default.Add, Icons.Outlined.Add, "Add data", false)
}