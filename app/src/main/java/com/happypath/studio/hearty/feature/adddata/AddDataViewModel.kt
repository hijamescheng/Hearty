package com.happypath.studio.hearty.feature.adddata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.AddMeasurementUseCase
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnArmLocationChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnDiastolicChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnNoteUpdate
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnSystolicChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.Submit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDataViewModel @Inject constructor(val addDataUseCase: AddMeasurementUseCase) :
    ViewModel() {

    private val _uistate = MutableStateFlow(AddDataUIState())
    val uistate = _uistate

    fun onEvent(event: AddDataFormEvent) {
        when (event) {
            is OnArmLocationChange -> updateArmLocation(event.armLocation)
            is OnNoteUpdate -> updateNote(event.note)
            is OnSystolicChange -> updateSystolic(event.systolic)
            is OnDiastolicChange -> updateDiastolic(event.diastolic)
            is Submit -> submitForm()
        }
    }

    fun submitForm() {
        viewModelScope.launch {
            addDataUseCase.invoke(_uistate.value.toBloodPressureMeasurement())
        }
    }

    fun updateArmLocation(value: Int) {
        _uistate.update {
            it.copy(armLocation = value)
        }
    }

    fun updateSystolic(value: Int) {
        _uistate.update {
            it.copy(systolic = value)
        }
    }

    fun updateDiastolic(value: Int) {
        _uistate.update {
            it.copy(diastolic = value)
        }
    }

    fun updateNote(value: String) {
        _uistate.update {
            it.copy(note = value)
        }
    }
}

sealed interface AddDataFormEvent {
    data class OnArmLocationChange(val armLocation: Int) : AddDataFormEvent
    data class OnNoteUpdate(val note: String) : AddDataFormEvent
    data class OnSystolicChange(val systolic: Int) : AddDataFormEvent
    data class OnDiastolicChange(val diastolic: Int) : AddDataFormEvent

    data object Submit : AddDataFormEvent
    // data class OnHeartRateChange(val diastolic: Int) : AddDataFormEvent
}