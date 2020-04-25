package au.com.hearty.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.hearty.data.dao.MeasurementDao
import au.com.hearty.model.Measurement
import au.com.hearty.util.Coroutines
import java.lang.Exception

class MeasurementDataRepository(private val measurementDao: MeasurementDao) :
    MeasurementRepositoryContract {

    override fun addMeasurement(measurement: Measurement): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>().apply { value = false }
        Coroutines.ioThenMain(
            work = { measurementDao.insert(measurement) },
            errorCallback = {},
            successCallback = { result.value = true }
        )
        return result
    }

    override fun removeMeasurements(vararg measurement: Measurement): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>().apply { value = false }
        Coroutines.ioThenMain(
            work = { measurementDao.deleteAll(*measurement) },
            errorCallback = {},
            successCallback = { result.value = true }
        )
        return result
    }

    override fun listAllMeasurement(): LiveData<List<Measurement>> = measurementDao.getAll()
}