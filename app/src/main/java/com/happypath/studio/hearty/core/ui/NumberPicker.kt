package com.happypath.studio.hearty.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
                        style = MaterialTheme.typography.bodyMedium,
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