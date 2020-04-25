package au.com.hearty.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import au.com.hearty.R
import au.com.hearty.data.RepositoryFactory
import au.com.hearty.databinding.ActivityAddMeasurementBinding
import au.com.hearty.model.Measurement
import au.com.hearty.viewmodel.AddMeasurementViewModel
import au.com.hearty.viewmodel.ViewModelFactory

class AddMeasurementActivity: BaseActivity<ActivityAddMeasurementBinding>() {

    override val showBackButton: Boolean = true

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory { AddMeasurementViewModel(RepositoryFactory().getMeasurementRepository(this)) }).get(
            AddMeasurementViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_measurement)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_measurement, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_save -> save()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        if (validate()) {
            val measurement = Measurement(
                systolic = binding.sysReading.text.toString().toInt(),
                diastolic = binding.diaReading.text.toString().toInt(),
                pulse = binding.pulseReading.text.toString().toInt(),
                weight = binding.weightEditText.text.toString().toInt()
            )
            viewModel.addMeasurement(measurement)
            finish()
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (binding.sysReading.text.toString().isNullOrEmpty()) {
            binding.sysTextInputLayout.error = getString(R.string.error_sys_empty_value)
            isValid = false
        }
        if (binding.diaReading.text.toString().isNullOrEmpty()) {
            binding.diaTextInputLayout.error = getString(R.string.error_dia_empty_value)
            isValid = false
        }
        if (binding.pulseReading.text.toString().isNullOrEmpty()) {
            binding.pulseTextInputLayout.error = getString(R.string.error_pulse_empty_value)
            isValid = false
        }
        if (binding.weightEditText.text.toString().isNullOrEmpty()) {
            binding.weigtTextInputLayout.error = getString(R.string.error_weight_empty_value)
            isValid = false
        }
        return isValid
    }
}