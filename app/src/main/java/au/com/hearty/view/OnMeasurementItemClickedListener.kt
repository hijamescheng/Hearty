package au.com.hearty.view

import au.com.hearty.model.MeasurementItemModel

interface OnMeasurementItemClickedListener {
    fun onItemClicked(item: MeasurementItemModel, position: Int)
}

interface OnMeasurementItemLongClickedListener {
    fun onItemLongClicked(item: MeasurementItemModel, position: Int)
}