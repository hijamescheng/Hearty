package au.com.hearty.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import au.com.hearty.util.DateTimeConverter
import java.util.*

@Entity(tableName = "measurement_history")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "systolic") val systolic: Int = 0,
    @ColumnInfo(name = "diastolic") val diastolic: Int = 0,
    @ColumnInfo(name = "pulse") val pulse: Int = 0,
    @ColumnInfo(name = "weight") val weight: Int = 0,
    @ColumnInfo(name = "dateTaken")
    val dateTaken: Date = Date()
)