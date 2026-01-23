package com.happypath.studio.hearty.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.happypath.studio.hearty.R

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        Row {
            OutlinedTextField(
                value = "James",
                onValueChange = {},
                label = { Text("Name") }
            )
        }
    }
}