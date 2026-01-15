package com.happypath.studio.hearty.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreenTopBar(navController: NavController) {
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }
    }, actions = {
        TextButton(onClick = {}) {
            Text("Save")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Text(
            "Add data", fontSize = 35.sp, modifier = Modifier.padding(start = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Time")
            Text("Today 2:47 PM")
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Blood pressure")
            Text("mmHg")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            var systolic by remember { mutableIntStateOf(120) }
            var diastolic by remember { mutableIntStateOf(85) }

            NumberPicker(
                range = 80..200,
                initialValue = systolic,
                modifier = Modifier.width(100.dp),
                onValueChange = { systolic = it })
            Text("/")
            NumberPicker(
                range = 50..150,
                initialValue = diastolic,
                modifier = Modifier.width(100.dp),
                onValueChange = { systolic = it })
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))
        Text("MEASUREMENT DETAILS", modifier = Modifier.padding(16.dp))
        HorizontalDivider()
    }
}

@SuppressLint("FrequentlyChangingValue")
@Composable
fun NumberPicker(
    range: IntRange,
    modifier: Modifier = Modifier,
    initialValue: Int = range.first,
    visibleItemsCount: Int = 3,
    onValueChange: (Int) -> Unit
) {
    require(visibleItemsCount % 2 == 1) {
        "visibleItemsCount must be odd"
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialValue - range.first
    )

    val flingBehavior = rememberSnapFlingBehavior(listState)
    val itemHeight = 48.dp
    val centerIndex = visibleItemsCount / 2

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            val value = range.first + index
            if (value in range) {
                onValueChange(value)
            }
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight * centerIndex)
        ) {
            items(range.toList()) { value ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val isSelected =
                        listState.firstVisibleItemIndex + centerIndex - 1 == value - range.first
                    Text(
                        text = value.toString(),
                        style = if (isSelected) MaterialTheme.typography.headlineSmall
                        else MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -itemHeight / 2) // top line
                .fillMaxWidth(0.8f), thickness = 2.dp, color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = itemHeight / 2) // bottom line
                .fillMaxWidth(0.8f), thickness = 2.dp, color = MaterialTheme.colorScheme.primary
        )
    }
}