package au.com.hearty.data

import androidx.lifecycle.LiveData
import au.com.hearty.model.Measurement

interface MeasurementRepositoryContract {

    fun addMeasurement(measurement: Measurement): LiveData<Boolean>

    fun removeMeasurements(vararg measurement: Measurement): LiveData<Boolean>

    fun listAllMeasurement(): LiveData<List<Measurement>>

//    fun getAllMeasurement(): LiveData<List<Measurement>>
}