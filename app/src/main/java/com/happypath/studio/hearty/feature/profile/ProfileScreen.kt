package com.happypath.studio.hearty.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.happypath.studio.hearty.R
import com.happypath.studio.hearty.core.ui.theme.Subtext
import com.happypath.studio.hearty.feature.home.cardColor

@Composable
fun ProfileScreen(innerPadding: PaddingValues, profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)
    ) {
        ProfileCard(
            stringResource(R.string.profile_height_title),
            profileState.heightText,
            RoundedCornerShape(
                topStart = dimensionResource(R.dimen.corner_radius_medium),
                topEnd = dimensionResource(R.dimen.corner_radius_medium)
            )
        )
        Spacer(modifier = Modifier.padding(2.dp))
        ProfileCard(
            stringResource(R.string.profile_weight_title),
            profileState.weightText,
            RoundedCornerShape(dimensionResource(R.dimen.corner_radius_small))
        )
        Spacer(modifier = Modifier.padding(2.dp))
        ProfileCard(
            stringResource(R.string.profile_birthday_title),
            profileState.birthdayText,
            RoundedCornerShape(dimensionResource(R.dimen.corner_radius_small))
        )
        Spacer(modifier = Modifier.padding(2.dp))
        ProfileCard(
            stringResource(R.string.profile_sex_title),
            profileState.sexText,
            RoundedCornerShape(
                bottomStart = dimensionResource(R.dimen.corner_radius_medium),
                bottomEnd = dimensionResource(R.dimen.corner_radius_medium)
            )
        )
    }
}

@Composable
fun ProfileCard(title: String, value: String, cornerShape: RoundedCornerShape) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = cardColor(),
        shape = cornerShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                color = Subtext
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}