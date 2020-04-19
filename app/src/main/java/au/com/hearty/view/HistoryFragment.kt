package au.com.hearty.view

import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import au.com.hearty.R
import au.com.hearty.databinding.ActivityMeasurementsBinding
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.util.AnimationUtils
import au.com.hearty.view.adapter.MeasurementListAdapter
import au.com.hearty.view.decorator.MeasurementItemDividerDecorator
import au.com.hearty.viewmodel.HistoryViewModel
import au.com.hearty.viewmodel.ViewModelFactory

class HistoryFragment : Fragment() {

    private val margin by lazy { resources.getDimensionPixelSize(R.dimen.margin) }
    private var actionMode: ActionMode? = null

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory()).get(
            HistoryViewModel::class.java
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<ActivityMeasurementsBinding>(
                inflater,
                R.layout.activity_measurements,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.recyclerView.apply {
            adapter = measurementListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MeasurementItemDividerDecorator(margin, context))
        }
        viewModel.getMeasurements()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onItemSelectionChanged.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                actionMode?.finish()
            } else {
                actionMode?.title = it.toString()
            }
        })
    }

    inner class ActionModeCallback: ActionMode.Callback {

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            viewModel.deleteSelectedItems()
            return true
        }

        override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
            activity?.apply {
                menuInflater?.inflate(R.menu.menu_delete, menu)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AnimationUtils.animateStatusBarColor(
                        this,
                        ResourcesCompat.getColor(resources, R.color.action_mode_status_bar_color, theme),
                        R.color.action_mode_status_bar_color
                    )
                }
            }
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return true
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            actionMode = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.apply {
                    AnimationUtils.animateStatusBarColor(
                        this,
                        ContextCompat.getColor(this, R.color.action_mode_status_bar_color),
                        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme),
                        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme)
                    )
                }
            }
            viewModel.clearSelection()
        }
    }

    private fun getOnMeasurementItemLongPressListener(): OnMeasurementItemLongClickedListener {
        return object : OnMeasurementItemLongClickedListener {
            override fun onItemLongClicked(item: MeasurementItemModel) {
                if (actionMode == null) {
                    actionMode = (context as AppCompatActivity).startActionMode(ActionModeCallback())
                    viewModel.initSelection(item.measurement.id)
                }
                true
            }
        }
    }

    private fun getOnMeasurementItemClickedListener(): OnMeasurementItemClickedListener {
        return object : OnMeasurementItemClickedListener {
            override fun onItemClicked(item: MeasurementItemModel) {
                if (actionMode == null) return
                if (!viewModel.isItemSelected(item.measurement.id)) {
                    viewModel.selectedItem(item.measurement.id)
                } else {
                    viewModel.unSelectItem(item.measurement.id)
                }
            }
        }
    }

    private val measurementListAdapter = MeasurementListAdapter(
        getOnMeasurementItemLongPressListener(),
        getOnMeasurementItemClickedListener()
    )
}