package au.com.hearty.view.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.hearty.model.Measurement
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.view.adapter.BaseRecyclerViewAdapter
import au.com.hearty.view.adapter.BaseViewHolder

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Any>?) {
    (listView.adapter as? BaseRecyclerViewAdapter<Any, BaseViewHolder<Any>>)?.submitList(items)
}