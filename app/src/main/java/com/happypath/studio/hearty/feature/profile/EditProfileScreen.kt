package com.happypath.studio.hearty.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.happypath.studio.hearty.core.ui.DatePicker
import com.happypath.studio.hearty.core.ui.NumberPicker
import com.happypath.studio.hearty.core.ui.theme.WindowBackground
import com.happypath.studio.hearty.feature.home.cardColor
import com.happypath.studio.hearty.feature.profile.ProfileEditViewModel.ProfileEvent.BirthdayChanged
import com.happypath.studio.hearty.feature.profile.ProfileEditViewModel.ProfileEvent.HeightChanged
import com.happypath.studio.hearty.feature.profile.ProfileEditViewModel.ProfileEvent.WeightChanged
import com.happypath.studio.hearty.util.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePage(innerPadding: PaddingValues, profileViewModel: ProfileEditViewModel) {
    val profileSate by profileViewModel.profileEditState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row {
            Box(modifier = Modifier.weight(1f)) {
                val showHeightDialog = remember { mutableStateOf(false) }
                if (showHeightDialog.value) {
                    ProfileDialog(
                        title = "Height (in cm)",
                        valueRange = 100..250,
                        initialValue = 170,
                        onValueSelected = {
                            profileViewModel.onEvent(HeightChanged(it))
                            showHeightDialog.value = false
                        },
                        onDismiss = { showHeightDialog.value = false }
                    )
                }
                OutlinedTextField(
                    readOnly = true,
                    label = { Text("Height (cm)") },
                    value = "${profileSate.height}",
                    onValueChange = {}
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            showHeightDialog.value = true
                        })
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                val showWeightDialog = remember { mutableStateOf(false) }
                if (showWeightDialog.value) {
                    ProfileDialog(
                        title = "Weight (in cm)",
                        valueRange = 30..300,
                        initialValue = 80,
                        onValueSelected = {
                            profileViewModel.onEvent(WeightChanged(it))
                            showWeightDialog.value = false
                        },
                        onDismiss = { showWeightDialog.value = false }
                    )
                }
                OutlinedTextField(
                    readOnly = true,
                    label = { Text("Weight (kg)") },
                    value = "${profileSate.weight}",
                    onValueChange = {}
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            showWeightDialog.value = true
                        })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box {
            val showDatePicker = remember { mutableStateOf(false) }
            if (showDatePicker.value) {
                DatePicker({
                    profileViewModel.onEvent(BirthdayChanged(it ?: 0))
                    showDatePicker.value = false
                }, {
                    showDatePicker.value = false
                })
            }
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("Birthday") },
                value = profileSate.birthday.toDateString("dd MMM yyyy"),
                onValueChange = {}
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        showDatePicker.value = true
                    })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            val showSexDialog = remember { mutableStateOf(false) }
            if (showSexDialog.value) {
                DatePicker({}, {
                    showSexDialog.value = false
                })
            }
            var expanded by remember { mutableStateOf(false) }
            val options = listOf("Female", "Male", "Other")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Sex") },
                    value = when (profileSate.sex) {
                        0 -> "Female"
                        1 -> "Male"
                        else -> "Other"
                    },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Sex")
                    },
                    onValueChange = {}
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    modifier = Modifier.background(WindowBackground),
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                profileViewModel.onEvent(
                                    ProfileEditViewModel.ProfileEvent.SexChanged(index)
                                )
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDialog(
    title: String,
    valueRange: IntRange,
    initialValue: Int,
    onValueSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedValue = remember { mutableIntStateOf(initialValue) }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = cardColor()
        ) {
            Text(
                title,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumberPicker(
                    modifier = Modifier.width(100.dp),
                    range = valueRange,
                    initialValue = initialValue,
                    onValueChange = {
                        selectedValue.intValue = it
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onValueSelected(selectedValue.intValue) }) {
                    Text("Ok")
                }
            }
        }
    }
}