package au.com.hearty.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import au.com.hearty.R
import au.com.hearty.databinding.ItemMeasurementRichBinding
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.view.OnMeasurementItemClickedListener
import au.com.hearty.view.OnMeasurementItemLongClickedListener

const val SELECTED = 1
const val UNSELECTED = 2

class MeasurementListAdapter constructor(
    private val onMeasurementItemLongPressListener: OnMeasurementItemLongClickedListener,
    private val onMeasurementItemClickListener: OnMeasurementItemClickedListener
) : BaseRecyclerViewAdapter<MeasurementItemModel, MeasurementListAdapter.MeasurementViewHolder>(
    MeasurementItemDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val binding =
            ItemMeasurementRichBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasurementViewHolder(binding, onMeasurementItemLongPressListener, onMeasurementItemClickListener)
    }

    class MeasurementViewHolder constructor(
        private val binding: ItemMeasurementRichBinding,
        private val onMeasurementItemLongPressListener: OnMeasurementItemLongClickedListener,
        private val onMeasurementItemClickListener: OnMeasurementItemClickedListener
    ) : BaseViewHolder<MeasurementItemModel>(binding.root) {

        override fun bind(model: MeasurementItemModel, position: Int) {
            binding.model = model
            Log.d("james", "$model")
            binding.root.setOnLongClickListener {
                Log.d("james", position.toString())
                onMeasurementItemLongPressListener.onItemLongClicked(model)
                true
            }
            binding.root.setOnClickListener {
                Log.d("james", position.toString())
                onMeasurementItemClickListener.onItemClicked(model)
            }
            if (model.isSelected) {
                binding.frame.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.measurement_item_selected_color))
            }
        }

        override fun bind(model: MeasurementItemModel, payloads: MutableList<Any>) {
            if (payloads.contains(SELECTED)) {
                Log.d("james", "${model}")
                binding.frame.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.measurement_item_selected_color))
            }
            if (payloads.contains(UNSELECTED)) {
                binding.frame.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            }
        }

        override fun unbind() {
            binding.root.setOnLongClickListener(null)
            binding.root.setOnClickListener(null)
        }
    }
}

class MeasurementItemDiffCallback : DiffUtil.ItemCallback<MeasurementItemModel>() {
    override fun areItemsTheSame(oldItem: MeasurementItemModel, newItem: MeasurementItemModel): Boolean =
        oldItem.measurement.id == newItem.measurement.id &&
                oldItem.isSelected == newItem.isSelected &&
                oldItem.isSelectionModeOn == newItem.isSelectionModeOn

    override fun areContentsTheSame(oldItem: MeasurementItemModel, newItem: MeasurementItemModel): Boolean {
        return oldItem.measurement == newItem.measurement &&
                oldItem.isSelected == newItem.isSelected &&
                oldItem.isSelectionModeOn == newItem.isSelectionModeOn
    }
}