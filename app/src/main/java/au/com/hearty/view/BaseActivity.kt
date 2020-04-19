package au.com.hearty.view

import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: T

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false) as T
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    protected fun enableTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}