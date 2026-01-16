package com.happypath.studio.hearty.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.ui.theme.DarkGreen
import com.happypath.studio.hearty.ui.theme.WindowBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        colors = topAppBarColor()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreenTopBar(navController: NavController) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        colors = topAppBarColor(),
        actions = {
            TextButton(onClick = {}) {
                Text(stringResource(R.string.add_measurement_save))
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarColor() = TopAppBarDefaults.topAppBarColors(
    containerColor = WindowBackground,   // background color
    titleContentColor = DarkGreen // text color
)