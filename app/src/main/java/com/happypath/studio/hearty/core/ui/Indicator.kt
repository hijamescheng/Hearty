package com.happypath.studio.hearty.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.happypath.studio.hearty.R

@Composable
fun Indicator(type: ReadingType) {
    val colors = when (type) {
        ReadingType.NORMAL -> Pair(Color(0xFFECFDF5), Color(0xFF077A59))
        ReadingType.ELEVATED -> Pair(Color(0xFFFFFBEB), Color(0xFFB5560D))
        ReadingType.HIGH -> Pair(Color(0xFFFEF2F2), Color(0xFFBC2727))
    }
    val text = when (type) {
        ReadingType.NORMAL -> stringResource(R.string.reading_normal)
        ReadingType.ELEVATED -> stringResource(R.string.reading_elevated)
        ReadingType.HIGH -> stringResource(R.string.reading_high)
    }

    Box(
        modifier = Modifier
            .background(
                color = colors.first, // background color
                shape = RoundedCornerShape(percent = 50) // pill shape
            )
            .padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            )
    ) {
        Text(
            text = text,
            color = colors.second,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

enum class ReadingType {
    NORMAL,
    ELEVATED,
    HIGH
}

@Preview
@Composable
fun PreviewNormalIndicator() {
    Column {
        Indicator(ReadingType.NORMAL)
        Indicator(ReadingType.ELEVATED)
        Indicator(ReadingType.HIGH)
    }
}