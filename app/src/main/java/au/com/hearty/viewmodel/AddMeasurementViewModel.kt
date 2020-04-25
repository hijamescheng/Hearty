package au.com.hearty.viewmodel

import androidx.lifecycle.ViewModel
import au.com.hearty.data.MeasurementDataRepository
import au.com.hearty.model.Measurement

class AddMeasurementViewModel (private val repository: MeasurementDataRepository) : ViewModel() {

    fun addMeasurement(measurement: Measurement) = repository.addMeasurement(measurement)
}