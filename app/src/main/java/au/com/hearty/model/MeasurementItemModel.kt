package au.com.hearty.model

data class MeasurementItemModel constructor(
    val measurement: Measurement,
    val isSelected: Boolean = false,
    val isSelectionModeOn: Boolean = false
)