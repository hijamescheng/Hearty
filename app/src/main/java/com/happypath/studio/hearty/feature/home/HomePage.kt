package com.happypath.studio.hearty.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.theme.CardBackground
import com.happypath.studio.hearty.core.ui.theme.Subtext
import com.happypath.studio.hearty.core.ui.theme.WindowBackground
import com.happypath.studio.hearty.util.toDate

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
    LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))) {
        items(items = uistate.list) { state ->
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = cardColor(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    state.createdAt.toDate(),
                    color = Subtext,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.systolic_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = "${state.systolic}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(stringResource(R.string.add_measurement_bp_unit), color = Subtext)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.diastolic_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = "${state.diastolic}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(stringResource(R.string.add_measurement_bp_unit), color = Subtext)
                    }
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.pulse_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = "${state.heartRate}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            stringResource(R.string.pulse_title),
                            color = Subtext
                        )
                    }
                }
            }
        }
        item {
            Box(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun cardColor() = CardDefaults.cardColors(
    containerColor = CardBackground
)