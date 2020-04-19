package au.com.hearty.view

import android.os.Bundle
import android.view.MenuItem
import au.com.hearty.R
import au.com.hearty.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var currentlySelectedTabId: Int? = null
    private val measurementFragment = HistoryFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationBar.setOnNavigationItemSelectedListener(bottomNavBarOnItemSelectedListener)
    }

    override fun onPause() {
        super.onPause()
        bottomNavigationBar.setOnNavigationItemSelectedListener(null)
    }

    private val bottomNavBarOnItemSelectedListener = object: BottomNavigationView.OnNavigationItemSelectedListener {

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
                val transation = supportFragmentManager.beginTransaction()
                transation.replace(R.id.contentFrame, it)
                transation.commit()
            }
            currentlySelectedTabId = id
        }
    }
}
