package au.com.hearty.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import au.com.hearty.R
import au.com.hearty.coordinator.BindingCoordinatorProvider
import au.com.hearty.coordinator.HistoryCoordinator
import au.com.hearty.data.RepositoryFactory
import au.com.hearty.databinding.ActivityMeasurementsBinding
import au.com.hearty.extension.refresh
import au.com.hearty.viewmodel.HistoryViewModel
import au.com.hearty.viewmodel.ViewModelFactory
import com.squareup.coordinators.Coordinators

class HistoryFragment : BaseFragment<ActivityMeasurementsBinding>() {

    override var layoutResId: Int = R.layout.activity_measurements

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory { HistoryViewModel(RepositoryFactory().getMeasurementRepository(this.requireContext())) }).get(
            HistoryViewModel::class.java
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val coordinator = HistoryCoordinator(this, viewModel)
        Coordinators.bind(
            binding.root,
            BindingCoordinatorProvider(coordinator)
        )
        return binding.root
    }
}