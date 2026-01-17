package com.happypath.studio.hearty.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.theme.WindowBackground

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
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
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
        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
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

@Composable
fun HomeTabScreen(
    innerPadding: PaddingValues,
    destination: HomeTabDestination,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val result = viewModel.uistate.collectAsStateWithLifecycle()
    when (result.value) {
        is HomePageUiState.Success -> HomeTabSuccessScreen(result.value as HomePageUiState.Success)
        is HomePageUiState.Loading -> {}
        is HomePageUiState.Empty -> {}
        is HomePageUiState.Error -> {}
    }
}

@Composable
fun HomeTabSuccessScreen(uistate: HomePageUiState.Success) {
    LazyColumn(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        items(items = uistate.list) { state ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("${state.createdAt}")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SYS")
                        Text("${state.systolic}")
                        Text("mmHg")
                    }
                    Column {
                        Text("DIA")
                        Text("${state.diastolic}")
                        Text("mmHg")
                    }
                    Column {
                        Text("Pulse")
                        Text("${state.heartRate}")
                        Text("BPM")
                    }
                }
            }
        }
    }
}