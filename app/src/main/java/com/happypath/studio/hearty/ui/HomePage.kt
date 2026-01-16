package com.happypath.studio.hearty.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.HomeTabScreen
import com.happypath.studio.hearty.ui.theme.WindowBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    innerPadding: PaddingValues
) {
    val navController = rememberNavController()
    val currentDestination by navController
        .currentBackStackEntryAsState()

    val selectedDestination = HomeTabDestination
        .entries
        .indexOfFirst {
            it.route == currentDestination?.destination?.route
        }
        .coerceAtLeast(0)

    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedDestination,
            modifier = Modifier.padding(innerPadding),
            containerColor = WindowBackground
        ) {
            HomeTabDestination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    },
                    text = {
                        Text(
                            text = destination.label,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        HomeTabNavHost(innerPadding, navController)
    }
}

@Composable
fun HomeTabNavHost(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeTabDestination.Day.route
    ) {
        composable(HomeTabDestination.Day.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Day)
        }
        composable(HomeTabDestination.Week.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Week)
        }
        composable(HomeTabDestination.Month.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Month)
        }
        composable(HomeTabDestination.Year.route) {
            HomeTabScreen(innerPadding, HomeTabDestination.Year)
        }
    }
}