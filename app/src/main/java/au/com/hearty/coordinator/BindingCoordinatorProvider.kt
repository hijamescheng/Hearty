package au.com.hearty.coordinator

import android.view.View
import androidx.databinding.ViewDataBinding
import com.squareup.coordinators.Coordinator
import com.squareup.coordinators.CoordinatorProvider

class BindingCoordinatorProvider<in V: View, B: ViewDataBinding> constructor(private val coordinator: BaseCoordinator<V, B>) :
    CoordinatorProvider {
    @Suppress("UNCHECKED_CAST")
    override fun provideCoordinator(view: View): Coordinator {
        coordinator.onBind(view as V)
        return coordinator
    }
}