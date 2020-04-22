package au.com.hearty.coordinator

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import au.com.hearty.R
import au.com.hearty.databinding.ActivityMainBinding
import au.com.hearty.view.HistoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeCoordinator constructor(val activity: AppCompatActivity) :
    BaseCoordinator<ConstraintLayout, ActivityMainBinding>() {

    private var currentlySelectedTabId: Int? = null
    private val measurementFragment = HistoryFragment.newInstance()

    override fun onBind(view: ConstraintLayout) {
        super.onBind(view)
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener(
            getBottomNavBarOnItemSelectedListener()
        )
    }

    private fun getBottomNavBarOnItemSelectedListener() =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                switchTabs(item.itemId)
                return true
            }

            private fun switchTabs(id: Int) {
                if (currentlySelectedTabId == id) return
                val fragment = when (id) {
                    R.id.nav_measurement -> measurementFragment
                    else -> null
                }
                fragment?.let {
                    val transation = activity.supportFragmentManager.beginTransaction()
                    transation.replace(R.id.contentFrame, it)
                    transation.commit()
                }
                currentlySelectedTabId = id
            }
        }
}