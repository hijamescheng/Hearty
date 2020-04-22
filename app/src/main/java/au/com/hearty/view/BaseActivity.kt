package au.com.hearty.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import au.com.hearty.R
import au.com.hearty.databinding.ActivityBaseBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected open val showBackButton = false
    protected lateinit var binding: T
    protected lateinit var baseBinding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_base, null, false)
        setContentView(baseBinding.root)
        setupToolbar()
    }

    override fun setContentView(layoutResID: Int) {
       baseBinding.mainContent.let {
           it.viewStub?.apply {
               layoutResource = layoutResID
               inflate()
               binding = it.binding as T
           }
       }
    }

    private fun setupToolbar() {
        setSupportActionBar(baseBinding.toolbar)
        supportActionBar?.apply {
            this.title = this@BaseActivity.title
            setDisplayHomeAsUpEnabled(showBackButton)
        }
    }

    fun getToolbar() = baseBinding.toolbar
}