package com.happypath.studio.hearty.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.happypath.studio.hearty.core.ui.theme.BottomNavBar
import com.happypath.studio.hearty.core.ui.theme.Pink40

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: Int) {
    var selectedDestination by rememberSaveable { mutableIntStateOf(currentRoute) }
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = BottomNavBar
    ) {
        Destination.entries.filter { it.isBottomNav }.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(destination.route)
                    selectedDestination = index
                },
                icon = {
                    if (selectedDestination == index) {
                        Icon(
                            destination.selectedIcon,
                            contentDescription = destination.contentDescription
                        )
                    } else {
                        Icon(
                            destination.unselectedIcon,
                            contentDescription = destination.contentDescription
                        )
                    }
                },
                label = {
                    Text(text = destination.label)
                },
                colors = navigationBarItemColors()
            )
        }
    }
}

@Composable
fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    indicatorColor = Pink40
)
