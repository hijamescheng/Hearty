package au.com.hearty.view.adapter

import android.view.View
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<M : Any, VH : BaseViewHolder<M>>(private val diffCallback: DiffUtil.ItemCallback<M>) :
    ListAdapter<M, VH>(diffCallback) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    @UiThread
    open fun notifyAllChanged(payload: Any?) {
        for (index in 0 until itemCount) {
            notifyItemChanged(index, payload)
        }
    }
}

abstract class BaseViewHolder<M : Any>(view: View) :
    RecyclerView.ViewHolder(view) {

    abstract fun bind(model: M)
    open fun bind(model: M, payloads: MutableList<Any>) {}
    abstract fun unbind()
}