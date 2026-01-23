package com.happypath.studio.hearty.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.happypath.studio.hearty.R

@Composable
fun HomePage(innerPadding: PaddingValues) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val uiState = homeViewModel.dayTabUiState.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { homeViewModel.onDateRangeChanged(true) }) {
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "previous"
                    )
                }
                Text(uiState.dateRangeText, fontWeight = FontWeight.W500)
                IconButton(
                    onClick = { homeViewModel.onDateRangeChanged(false) },
                    enabled = uiState.isNextButtonEnabled
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "previous"
                    )
                }
            }
        }
        if (uiState.isEmpty) {
            item {
                NoDataScreen()
            }
        } else {
            item {
                HomeSummaryView(uiState)
            }
        }

    }
}

@Composable
fun HomeSummaryView(
    uiState: HomeTabPageUiState
) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))) {
        uiState.list.forEach { AvgMeasurementCard(it, -1) }
    }
}
