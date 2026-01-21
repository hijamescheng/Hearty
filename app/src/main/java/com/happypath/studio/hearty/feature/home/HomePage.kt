package com.happypath.studio.hearty.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
        val homeViewModel = hiltViewModel<HomeViewModel>()
        HomeTabNavHost(innerPadding, navController, homeViewModel)
    }
}

@Composable
fun HomeTabNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    NavHost(
        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        navController = navController,
        startDestination = HomeTabDestination.Day.route
    ) {
        composable(HomeTabDestination.Day.route) {
            val uistate = homeViewModel.dayTabUiState.collectAsStateWithLifecycle().value
            HomeTabScreen(
                uiState = uistate,
                onPrevious = { homeViewModel.onDayRangeChanged(true, MeasurementScope.DAY) },
                onNext = { homeViewModel.onDayRangeChanged(false, MeasurementScope.DAY) }
            )
        }
        composable(HomeTabDestination.Week.route) {
            val uistate = homeViewModel.weekTabUiState.collectAsStateWithLifecycle().value
            HomeTabScreen(
                uiState = uistate,
                onPrevious = { homeViewModel.onDayRangeChanged(true, MeasurementScope.WEEK) },
                onNext = { homeViewModel.onDayRangeChanged(false, MeasurementScope.WEEK) }
            )
        }
        composable(HomeTabDestination.Month.route) {
            val uistate = homeViewModel.monthTabUiState.collectAsStateWithLifecycle().value
            HomeTabScreen(
                uiState = uistate,
                onPrevious = { homeViewModel.onDayRangeChanged(true, MeasurementScope.MONTH) },
                onNext = { homeViewModel.onDayRangeChanged(false, MeasurementScope.MONTH) }
            )
        }
        composable(HomeTabDestination.Year.route) {
            val uistate = homeViewModel.yearTabUiState.collectAsStateWithLifecycle().value
            HomeTabScreen(
                uiState = uistate,
                onPrevious = { homeViewModel.onDayRangeChanged(true, MeasurementScope.YEAR) },
                onNext = { homeViewModel.onDayRangeChanged(false, MeasurementScope.YEAR) }
            )
        }
    }
}

@Composable
fun HomeTabScreen(
    uiState: HomeTabPageUiState,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onPrevious() }) {
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "previous"
                    )
                }
                Text(uiState.dateRangeText, fontWeight = FontWeight.W500)
                IconButton(onClick = { onNext() }, enabled = uiState.isNextButtonEnabled) {
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "previous"
                    )
                }
            }
        }
        item {
            HomeTabSummary(uiState.list)
        }
    }

}

@Composable
fun NoDataScreen() {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp)) {
        Text(
            stringResource(R.string.no_data_title),
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            stringResource(R.string.no_data_description),
            color = Subtext
        )
    }
}

@Composable
fun HomeTabSummary(list: List<AvgMeasurement>) {
    NoDataScreen()
    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))) {
        list.forEachIndexed { index, avgMeasurement ->
            val position = if (index == 0) 0 else if (index == list.size - 1) 2 else 1
            AvgMeasurementCard(avgMeasurement, position)
        }
    }
}

@Composable
fun AvgMeasurementCard(avgMeasurement: AvgMeasurement, position: Int) {

    val cornerShape = when (position) {
        0 -> RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 5.dp,
            bottomEnd = 5.dp
        )

        1 -> RoundedCornerShape(5.dp)
        else -> RoundedCornerShape(
            topStart = 5.dp,
            topEnd = 5.dp,
            bottomStart = 20.dp,
            bottomEnd = 20.dp
        )
    }

    Card(
        modifier = Modifier.padding(top = 8.dp),
        colors = cardColor(),
        shape = cornerShape
    ) {
        Text(
            avgMeasurement.dateText,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                    text = avgMeasurement.avgSys,
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
                    text = avgMeasurement.avgDia,
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
                    text = avgMeasurement.avgPulse,
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

@Composable
fun cardColor() = CardDefaults.cardColors(
    containerColor = CardBackground
)