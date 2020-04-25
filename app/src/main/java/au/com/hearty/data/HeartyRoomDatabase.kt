package au.com.hearty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import au.com.hearty.data.dao.MeasurementDao
import au.com.hearty.model.Measurement
import au.com.hearty.util.DateTimeConverter

const val DBName = "hearty-database"

@Database(entities = [Measurement::class], version = 1)
@TypeConverters(DateTimeConverter::class)
abstract class HeartyRoomDatabase : RoomDatabase() {

    abstract fun measurementDao(): MeasurementDao
}