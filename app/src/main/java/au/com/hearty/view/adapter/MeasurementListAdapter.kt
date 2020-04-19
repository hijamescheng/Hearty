package au.com.hearty.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import au.com.hearty.databinding.ItemMeasurementRichBinding
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.view.OnMeasurementItemClickedListener
import au.com.hearty.view.OnMeasurementItemLongClickedListener

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

        override fun bind(model: MeasurementItemModel) {
            binding.model = model
            binding.root.setOnLongClickListener {
                onMeasurementItemLongPressListener.onItemLongClicked(model)
                true
            }
            binding.root.setOnClickListener {
                onMeasurementItemClickListener.onItemClicked(model)
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