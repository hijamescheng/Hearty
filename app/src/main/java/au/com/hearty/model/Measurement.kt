package au.com.hearty.model

data class Measurement(
    val id: Int,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
    val weight: Int,
    val dateTaken: String
)