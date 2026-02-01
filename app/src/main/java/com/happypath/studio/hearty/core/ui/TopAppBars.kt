package com.happypath.studio.hearty.core.ui

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
import com.happypath.studio.hearty.core.ui.theme.DarkGreen
import com.happypath.studio.hearty.core.ui.theme.WindowBackground
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent
import com.happypath.studio.hearty.feature.adddata.AddDataViewModel
import com.happypath.studio.hearty.feature.profile.ProfileViewModel

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
fun AddDataScreenTopBar(navController: NavController, viewModel: AddDataViewModel) {
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
            TextButton(onClick = {
                viewModel.onEvent(AddDataFormEvent.Submit)
                navController.popBackStack()
            }) {
                Text(stringResource(R.string.add_measurement_save))
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(viewModel: ProfileViewModel, onEdit: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        colors = topAppBarColor(),
        actions = {
            TextButton(onClick = {
                onEdit()
            }) {
                Text(stringResource(R.string.profile_edit))
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenTopBar(navController: NavController, onSave: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        colors = topAppBarColor(),
        actions = {
            TextButton(onClick = {
                onSave()
            }) {
                Text(stringResource(R.string.profile_save))
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarColor() = TopAppBarDefaults.topAppBarColors(
    containerColor = WindowBackground,   // background color
    titleContentColor = DarkGreen // text color
)