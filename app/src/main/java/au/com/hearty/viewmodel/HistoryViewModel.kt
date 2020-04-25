package au.com.hearty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.hearty.data.MeasurementRepositoryContract
import au.com.hearty.model.Measurement

class HistoryViewModel (private val repository: MeasurementRepositoryContract): ViewModel() {

    val readingHistories: LiveData<List<Measurement>> = repository.listAllMeasurement()

    private val selectedItemIds = mutableSetOf<Int>()
    val onItemSelectionChanged = MutableLiveData<Int>().apply { value = 0 }

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

    fun getPosition(id: Int): Int {
        if (readingHistories.value.isNullOrEmpty()) return -1
        readingHistories.value?.forEachIndexed { index, item ->
            if (item.id == id) return index
        }
        return -1
    }

    fun deleteSelectedItems(): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>().apply { value = false }
        val itemsToBeDeleted = selectedItemIds.map { Measurement(id = it) }
        if (selectedItemIds.size > 0) {
            repository.removeMeasurements(*itemsToBeDeleted.toTypedArray())
            selectedItemIds.clear()
            onItemSelectionChanged.value = selectedItemIds.size
            result.value = true
        }
        return result
    }
}