package com.happypath.studio.hearty.feature.adddata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.NumberPicker
import com.happypath.studio.hearty.core.ui.theme.CardBackground
import com.happypath.studio.hearty.feature.home.cardColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(innerPadding: PaddingValues, viewModel: AddDataViewModel) {
    val uiState = viewModel.uistate.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        Text(
            text = stringResource(R.string.add_measurement_title),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            fontSize = 35.sp,
        )
        HorizontalDivider(modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Time")

            Row {
                DatePickerExample(onDateSelected = {})

                var showDialog by remember { mutableStateOf(false) }
                TextButton(onClick = { showDialog = true }) { Text("12:00") }

                if (showDialog) {
                    EnterTime(onConfirm = { showDialog = false }, onDismiss = {
                        showDialog = false
                    })
                }
            }
        }
        HorizontalDivider()
        SelectBloodPressureRecord(
            systolic = uiState.value.systolic,
            diastolic = uiState.value.diastolic,
            onSystolicChange = {
                viewModel.onEvent(AddDataFormEvent.OnSystolicChange(it))
            },
            onDiastolicChange = {
                viewModel.onEvent(AddDataFormEvent.OnDiastolicChange(it))
            }
        )
        HorizontalDivider(modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)))
        Text(
            stringResource(R.string.add_measurement_details_title),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.add_measurement_arm_location))
            ArmLocationSegButtons(uiState.value.armLocation) {
                viewModel.onEvent(AddDataFormEvent.OnArmLocationChange(it))
            }
        }
        AddNote(uiState.value.note) {
            viewModel.onEvent(AddDataFormEvent.OnNoteUpdate(it))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExample(
    onDateSelected: (LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    TextButton(onClick = { showPicker = true }) {
        Text("Time")
    }

    if (showPicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(date)
                        }
                        showPicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterTime(
    onConfirm: () -> Unit,
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
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = cardColor()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Enter time")
                TimeInput(
                    modifier = Modifier.padding(top = 16.dp),
                    state = timePickerState,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun AddNote(note: String, onNoteChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        Text(stringResource(R.string.add_measurement_note))
        BasicTextField(
            value = note,
            onValueChange = { onNoteChange(it) },
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.padding_medium))
                .height(120.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = CardBackground),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun SelectBloodPressureRecord(
    systolic: Int,
    diastolic: Int,
    onSystolicChange: (Int) -> Unit,
    onDiastolicChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.add_measurement_blood_pressure))
        Text(stringResource(R.string.add_measurement_bp_unit))
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        NumberPicker(
            range = 80..200,
            initialValue = systolic,
            modifier = Modifier.width(100.dp),
            onValueChange = { onSystolicChange(it) })
        Text(stringResource(R.string.add_measurement_separator))
        NumberPicker(
            range = 50..150,
            initialValue = diastolic,
            modifier = Modifier.width(100.dp),
            onValueChange = { onDiastolicChange(it) })
    }
}

@Composable
fun ArmLocationSegButtons(armLocation: Int, onValueChange: (Int) -> Unit) {
    val options = stringArrayResource(R.array.arm_location_values)

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onValueChange(index) },
                selected = index == armLocation,
                label = { Text(label) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondary,
                    inactiveContainerColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}