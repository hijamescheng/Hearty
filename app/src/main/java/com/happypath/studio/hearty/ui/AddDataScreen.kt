package com.happypath.studio.hearty.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen() {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { /* back */ }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            },
            actions = {
                TextButton(onClick = {}) {
                    Text("Save")
                }
            }
        )
        Row {
            Text("Add data", fontSize = 35.sp, modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Preview
@Composable
fun preview() {
    AddDataScreen()
}