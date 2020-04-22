package au.com.hearty.view

import au.com.hearty.model.MeasurementItemModel

interface OnMeasurementItemClickedListener {
    fun onItemClicked(item: MeasurementItemModel)
}

interface OnMeasurementItemLongClickedListener {
    fun onItemLongClicked(item: MeasurementItemModel)
}