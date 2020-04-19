package au.com.hearty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import au.com.hearty.R
import au.com.hearty.coordinator.BindingCoordinatorProvider
import au.com.hearty.coordinator.HistoryCoordinator
import au.com.hearty.databinding.ActivityMeasurementsBinding
import au.com.hearty.viewmodel.HistoryViewModel
import au.com.hearty.viewmodel.ViewModelFactory
import com.squareup.coordinators.Coordinators

class HistoryFragment : BaseFragment<ActivityMeasurementsBinding>() {

    override var layoutResId: Int = R.layout.activity_measurements

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory()).get(
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
        Coordinators.bind(binding.root, BindingCoordinatorProvider(HistoryCoordinator(this, viewModel)))
        return binding.root
    }
}