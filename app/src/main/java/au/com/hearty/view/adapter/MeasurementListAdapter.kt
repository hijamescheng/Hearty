package au.com.hearty.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import au.com.hearty.R
import au.com.hearty.databinding.ItemMeasurementRichBinding
import au.com.hearty.model.Measurement
import au.com.hearty.view.OnMeasurementItemClickedListener
import au.com.hearty.view.OnMeasurementItemLongClickedListener

const val SELECTED = 1
const val UNSELECTED = 2

class MeasurementListAdapter constructor(
    private val onMeasurementItemLongPressListener: OnMeasurementItemLongClickedListener,
    private val onMeasurementItemClickListener: OnMeasurementItemClickedListener
) : BaseRecyclerViewAdapter<Measurement, MeasurementListAdapter.MeasurementViewHolder>(
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
    ) : BaseViewHolder<Measurement>(binding.root) {

        override fun bind(model: Measurement, position: Int) {
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
        }

        override fun bind(model: Measurement, payloads: MutableList<Any>) {
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

class MeasurementItemDiffCallback : DiffUtil.ItemCallback<Measurement>() {
    override fun areItemsTheSame(oldItem: Measurement, newItem: Measurement): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Measurement, newItem: Measurement): Boolean {
        return oldItem == newItem
    }
}