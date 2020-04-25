package au.com.hearty.data

import android.content.Context

class RepositoryFactory {

    fun getMeasurementRepository(context: Context): MeasurementDataRepository =
        MeasurementDataRepository(DatabaseFactory.getDatabase(context).measurementDao())
}