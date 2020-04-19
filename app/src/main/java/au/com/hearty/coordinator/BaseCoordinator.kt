package au.com.hearty.coordinator

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.squareup.coordinators.Coordinator


@Suppress("UNCHECKED_CAST")
abstract class BaseCoordinator<in V: View, B: ViewDataBinding> : Coordinator(), LifecycleObserver {
    // Set to true once onResumeAttached() has been called and false once onPauseAttached() has been called
    private var resumeAttached: Boolean = false

    // Set to true/false based on standard ON_RESUME/ON_PAUSE lifecycle
    internal var resumed: Boolean = false
        private set

    protected var binding: B? = null

    open fun onBind(view: V) {
        binding = DataBindingUtil.getBinding<B>(view)
    }

    open fun onAttach(view: V) {}

    open fun onDetach(view: V) {}

    open fun onResumeAttached() {}

    open fun onPauseAttached() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun resume() {
        resumed = true
        if (canResumeAttach()) {
            onResumeAttached()
            resumeAttached = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun pause() {
        if (isAttached && resumeAttached) {
            onPauseAttached()
            resumeAttached = false
        }
        resumed = false
    }

    override fun attach(view: View) {
        onAttach(view as V)

        if (canResumeAttach()) {
            onResumeAttached()
            resumeAttached = true
        }
    }

    override fun detach(view: View) {
        if (resumeAttached) onPauseAttached()
        resumeAttached = false

        onDetach(view as V)
    }

    internal open fun canResumeAttach(): Boolean {
        return isAttached && !resumeAttached
    }
}