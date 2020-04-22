package au.com.hearty.view

import android.os.Bundle
import au.com.hearty.R
import au.com.hearty.databinding.ActivityAddMeasurementBinding

class AddMeasurementActivity: BaseActivity<ActivityAddMeasurementBinding>() {

    override val showBackButton: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_measurement)
    }
}