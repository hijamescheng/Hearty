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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.NumberPicker
import com.happypath.studio.hearty.core.ui.theme.Gray


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
            Text("Today 2:47 PM")
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
                .background(color = Gray),
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