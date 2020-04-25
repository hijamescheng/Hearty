package au.com.hearty.view.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.hearty.model.Measurement
import au.com.hearty.model.MeasurementItemModel
import au.com.hearty.view.adapter.BaseRecyclerViewAdapter
import au.com.hearty.view.adapter.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

val timeFormat = SimpleDateFormat("hh:mm aaa")
val dateFormat = SimpleDateFormat("dd/mm/yy")

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Any>?) {
    (listView.adapter as? BaseRecyclerViewAdapter<Any, BaseViewHolder<Any>>)?.submitList(items)
}

@BindingAdapter("app:time_added")
fun setTimeAdded(textView: TextView, dateSaved: Date) {
    textView.text = timeFormat.format(dateSaved)
}

@BindingAdapter("app:date_added")
fun setDateAdded(textView: TextView, dateSaved: Date) {
    textView.text = when (daysInBetween(dateSaved, Date())) {
        0 -> "Today"
        1 -> "Yesterday"
        else -> dateFormat.format(dateSaved)
    }
}

private fun daysInBetween(dateSaved: Date, today: Date): Int {
    val dateSavedCalendar = Calendar.getInstance()
    val todayCalendar = Calendar.getInstance()
    dateSavedCalendar.time = dateSaved
    todayCalendar.time = today

    if (dateSavedCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        dateSavedCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)) {
        return todayCalendar.get(Calendar.DAY_OF_MONTH) - dateSavedCalendar.get(Calendar.DAY_OF_MONTH)
    }
    return -1
}