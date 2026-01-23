package com.happypath.studio.hearty.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.theme.WindowBackground
import com.happypath.studio.hearty.feature.home.cardColor
import java.time.Instant
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelected: (Long?) -> Unit,
    onDialogDismiss: () -> Unit,
) {
    val todayMillis = Instant.now().toEpochMilli()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis
    )
    Dialog(
        onDismissRequest = { onDialogDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.extraLarge,
            color = WindowBackground
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = WindowBackground
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(
                            end = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_medium)
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDialogDismiss() }) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(datePickerState.selectedDateMillis)
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier.fillMaxWidth(),
            colors = cardColor()
        ) {
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                Text("Enter time")
                TimeInput(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
                    state = timePickerState,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        onConfirm(timePickerState.hour, timePickerState.minute)
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}
