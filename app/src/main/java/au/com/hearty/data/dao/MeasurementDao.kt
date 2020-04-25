package au.com.hearty.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import au.com.hearty.model.Measurement

@Dao
interface MeasurementDao {

    @Query("SELECT * FROM measurement_history order by dateTaken DESC")
    fun getAll(): LiveData<List<Measurement>>

    @Insert
    fun insert(measurement: Measurement)

    @Delete
    fun deleteAll(vararg measurement: Measurement)
}