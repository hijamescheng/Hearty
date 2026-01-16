package com.happypath.studio.hearty.ui

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
import androidx.compose.runtime.mutableIntStateOf
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
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.ui.theme.Gray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(innerPadding: PaddingValues) {
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
            var systolic by remember { mutableIntStateOf(120) }
            var diastolic by remember { mutableIntStateOf(85) }

            NumberPicker(
                range = 80..200,
                initialValue = systolic,
                modifier = Modifier.width(100.dp),
                onValueChange = { systolic = it })
            Text(stringResource(R.string.add_measurement_separator))
            NumberPicker(
                range = 50..150,
                initialValue = diastolic,
                modifier = Modifier.width(100.dp),
                onValueChange = { systolic = it })
        }
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
            ArmLocationSegButtons()
        }
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(stringResource(R.string.add_measurement_note))
            var text by remember { mutableStateOf("") }
            BasicTextField(
                value = text,
                onValueChange = { text = it },
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
}

@Composable
fun ArmLocationSegButtons() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = stringArrayResource(R.array.arm_location_values)

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label = { Text(label) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondary,
                    inactiveContainerColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}