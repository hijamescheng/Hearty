package au.com.hearty.view

import au.com.hearty.model.Measurement

interface OnMeasurementItemClickedListener {
    fun onItemClicked(item: Measurement)
}

interface OnMeasurementItemLongClickedListener {
    fun onItemLongClicked(item: Measurement)
}