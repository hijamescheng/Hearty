package au.com.hearty.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.hearty.model.Measurement
import au.com.hearty.model.MeasurementItemModel

class HistoryViewModel : ViewModel() {

    val readingHistories = MutableLiveData<List<MeasurementItemModel>>()
    private val selectedItemIds = mutableSetOf<Int>()
    val onItemSelectionChanged = MutableLiveData<Int>().apply { value = 0 }

    fun getMeasurements() {
        val list = listOf(
            MeasurementItemModel(Measurement(1, 119, 80, 66, 80, "18/04/2020 8:40 pm")),
            MeasurementItemModel(Measurement(2, 122, 87, 69, 80, "18/04/2020 8:39 pm")),
            MeasurementItemModel(Measurement(3, 129, 78, 67, 80, "18/04/2020 8:38 pm")),
            MeasurementItemModel(Measurement(4, 135, 89, 87, 80, "18/04/2020 8:37 pm")),
            MeasurementItemModel(Measurement(5, 121, 85, 68, 80, "18/04/2020 8:36 pm")),
            MeasurementItemModel(Measurement(6, 139, 80, 78, 80, "18/04/2020 8:35 pm"))
        )
        readingHistories.value = list
    }

    fun isItemSelected(id: Int): Boolean = selectedItemIds.contains(id)

    fun selectedItem(id: Int) {
        selectedItemIds.add(id)
        onItemSelectionChanged.value = selectedItemIds.size
    }

    fun unSelectItem(id: Int) {
        selectedItemIds.remove(id)
        onItemSelectionChanged.value = selectedItemIds.size
    }

    fun initSelection(id: Int) {
        selectedItemIds.add(id)
        onItemSelectionChanged.value = selectedItemIds.size
    }

    fun clearSelection() {
        selectedItemIds.clear()
    }

    fun deleteSelectedItems() {
        if (selectedItemIds.size > 0) {
            readingHistories.value =
                readingHistories.value?.filterNot { selectedItemIds.contains(it.measurement.id) }
            selectedItemIds.clear()
            onItemSelectionChanged.value = selectedItemIds.size
        }
    }
}