package au.com.hearty.util

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi

class AnimationUtils {

    companion object {

        @JvmStatic
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun animateStatusBarColor(
            activity: Activity,
            sourceColor: Int,
            targetColor: Int
        ) {
            animateStatusBarColor(activity, sourceColor, targetColor, -1)
        }

        @JvmStatic
        fun animateStatusBarColor(
            activity: Activity,
            sourceColor: Int,
            targetColor: Int,
            finalColor: Int
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val anim =
                    ValueAnimator.ofObject(ArgbEvaluator(), sourceColor, targetColor)
                anim.duration =
                    activity.resources.getInteger(R.integer.config_mediumAnimTime).toLong()
                anim.addUpdateListener { animation ->
                    // has to be anonymous class, not lambda because of lint
                    activity.window.statusBarColor = animation.animatedValue as Int
                }
                if (finalColor > -1) {
                    anim.addListener(object : AnimatorListenerAdapter() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onAnimationEnd(animation: Animator) {
                            activity.window.statusBarColor = finalColor
                        }
                    })
                }
                anim.start()
            }
        }
    }
}