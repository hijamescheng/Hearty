package au.com.hearty.coordinator

import android.content.Intent
import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.com.hearty.R
import au.com.hearty.databinding.ActivityMeasurementsBinding
import au.com.hearty.extension.refresh
import au.com.hearty.model.Measurement
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.util.AnimationUtils
import au.com.hearty.view.AddMeasurementActivity
import au.com.hearty.view.MainActivity
import au.com.hearty.view.OnMeasurementItemClickedListener
import au.com.hearty.view.OnMeasurementItemLongClickedListener
import au.com.hearty.view.adapter.MeasurementListAdapter
import au.com.hearty.view.adapter.SELECTED
import au.com.hearty.view.adapter.UNSELECTED
import au.com.hearty.view.decorator.MeasurementItemDividerDecorator
import au.com.hearty.viewmodel.HistoryViewModel

class HistoryCoordinator constructor(
    private val fragment: Fragment,
    private val viewModel: HistoryViewModel
) : BaseCoordinator<CoordinatorLayout, ActivityMeasurementsBinding>() {

    private var actionMode: ActionMode? = null

    override fun onBind(view: CoordinatorLayout) {
        super.onBind(view)
        binding.apply {
            viewModel = this@HistoryCoordinator.viewModel
            recyclerView.apply {
                adapter = measurementListAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(MeasurementItemDividerDecorator(context))
            }
            addButton.setOnClickListener(saveButtonOnClickListener)
        }

        viewModel.onItemSelectionChanged.observe(fragment.viewLifecycleOwner, Observer {
            if (it == 0) {
                actionMode?.finish()
            } else {
                actionMode?.title = it.toString()
            }
        })

        binding.recyclerView.addOnScrollListener(recyclerViewOnScrollListener)
    }

    private val saveButtonOnClickListener = View.OnClickListener {
        val intent = Intent(fragment.context, AddMeasurementActivity::class.java)
        fragment.context?.startActivity(intent)
    }

    private val recyclerViewOnScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && binding.addButton.isVisible) {
                binding.addButton.hide()
            } else if (dy < 0 && !binding.addButton.isVisible) {
                binding.addButton.show()
            }
        }
    }

    private fun getOnMeasurementItemLongPressListener(): OnMeasurementItemLongClickedListener {
        return object : OnMeasurementItemLongClickedListener {
            override fun onItemLongClicked(item: Measurement) {
                if (actionMode == null) {
                    actionMode = (fragment.activity as MainActivity).getToolbar().startActionMode(ActionModeCallback())
                    viewModel.initSelection(item.id)
                    val itemPosition = viewModel.getPosition(item.id)
                    if (itemPosition >= 0) {
                        measurementListAdapter.notifyItemChanged(itemPosition, SELECTED)
                    }
                }
                true
            }
        }
    }

    private fun getOnMeasurementItemClickedListener(): OnMeasurementItemClickedListener {
        return object : OnMeasurementItemClickedListener {
            override fun onItemClicked(item: Measurement) {
                if (actionMode == null) return
                val payload = if (!viewModel.isItemSelected(item.id)) {
                    viewModel.selectedItem(item.id)
                    SELECTED
                } else {
                    viewModel.unSelectItem(item.id)
                    UNSELECTED
                }
                val itemPosition = viewModel.getPosition(item.id)
                if (itemPosition >= 0) {
                    measurementListAdapter.notifyItemChanged(itemPosition, payload)
                }
            }
        }
    }

    private val measurementListAdapter = MeasurementListAdapter(
        getOnMeasurementItemLongPressListener(),
        getOnMeasurementItemClickedListener()
    )

    inner class ActionModeCallback : ActionMode.Callback {

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            viewModel.deleteSelectedItems()
            return true
        }

        override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
            fragment.activity?.apply {
                menuInflater?.inflate(R.menu.menu_delete, menu)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AnimationUtils.animateStatusBarColor(
                        this,
                        ResourcesCompat.getColor(
                            resources,
                            R.color.action_mode_status_bar_color,
                            theme
                        ),
                        R.color.action_mode_status_bar_color
                    )
                }
                binding.addButton.visibility = View.GONE
            }
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return true
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            actionMode = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.activity?.apply {
                    AnimationUtils.animateStatusBarColor(
                        this,
                        ContextCompat.getColor(this, R.color.action_mode_status_bar_color),
                        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme),
                        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme)
                    )
                }
            }
            viewModel.clearSelection()
            measurementListAdapter.notifyAllChanged(UNSELECTED)
            binding.addButton.visibility = View.VISIBLE
        }
    }
}