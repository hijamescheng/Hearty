package au.com.hearty.view

import android.os.Bundle
import au.com.hearty.R
import au.com.hearty.coordinator.BindingCoordinatorProvider
import au.com.hearty.coordinator.HomeCoordinator
import au.com.hearty.databinding.ActivityMainBinding
import com.squareup.coordinators.Coordinators

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Coordinators.bind(binding.root, BindingCoordinatorProvider(HomeCoordinator(this)))
    }
}
