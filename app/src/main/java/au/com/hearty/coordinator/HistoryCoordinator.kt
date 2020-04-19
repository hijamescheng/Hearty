package au.com.hearty.coordinator

import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import au.com.hearty.R
import au.com.hearty.databinding.ActivityMeasurementsBinding
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.util.AnimationUtils
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
        viewModel.getMeasurements()
        binding?.viewModel = viewModel
        binding?.recyclerView?.apply {
            adapter = measurementListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MeasurementItemDividerDecorator(context))
        }

        viewModel.onItemSelectionChanged.observe(fragment.viewLifecycleOwner, Observer {
            if (it == 0) {
                actionMode?.finish()
            } else {
                actionMode?.title = it.toString()
            }
        })
    }

    private fun getOnMeasurementItemLongPressListener(): OnMeasurementItemLongClickedListener {
        return object : OnMeasurementItemLongClickedListener {
            override fun onItemLongClicked(item: MeasurementItemModel, position: Int) {
                if (actionMode == null) {
                    actionMode = fragment.activity?.startActionMode(ActionModeCallback())
                    viewModel.initSelection(item.measurement.id)
                    measurementListAdapter.notifyItemChanged(position, SELECTED)
                }
                true
            }
        }
    }

    private fun getOnMeasurementItemClickedListener(): OnMeasurementItemClickedListener {
        return object : OnMeasurementItemClickedListener {
            override fun onItemClicked(item: MeasurementItemModel, position: Int) {
                if (actionMode == null) return
                if (!viewModel.isItemSelected(item.measurement.id)) {
                    viewModel.selectedItem(item.measurement.id)
                    measurementListAdapter.notifyItemChanged(position, SELECTED)
                } else {
                    viewModel.unSelectItem(item.measurement.id)
                    measurementListAdapter.notifyItemChanged(position, UNSELECTED)
                }
            }
        }
    }

    private val measurementListAdapter = MeasurementListAdapter(
        getOnMeasurementItemLongPressListener(),
        getOnMeasurementItemClickedListener()
    )

    inner class ActionModeCallback: ActionMode.Callback {

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
                        ResourcesCompat.getColor(resources, R.color.action_mode_status_bar_color, theme),
                        R.color.action_mode_status_bar_color
                    )
                }
                binding?.addButton?.visibility = View.GONE
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
            binding?.addButton?.visibility = View.VISIBLE
        }
    }
}