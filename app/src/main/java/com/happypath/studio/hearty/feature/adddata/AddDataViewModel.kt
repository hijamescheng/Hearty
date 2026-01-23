package com.happypath.studio.hearty.feature.adddata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.AddMeasurementUseCase
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnArmLocationChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnDatePickerDismiss
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnDatePickerShow
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnDateSelected
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnDiastolicChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnNoteUpdate
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnSystolicChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnPulseChange
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnTimePickerDismiss
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnTimePickerShow
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.OnTimeSelected
import com.happypath.studio.hearty.feature.adddata.AddDataFormEvent.Submit
import com.happypath.studio.hearty.util.formatDateWithTodayYesterday
import com.happypath.studio.hearty.util.updateDateTimeWithHourMinute
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

    private var _selectedDate = System.currentTimeMillis()
    private var _selectedHour = 1
    private var _selectedMinute = 1

    fun onEvent(event: AddDataFormEvent) {
        when (event) {
            is OnArmLocationChange -> _uistate.update {
                it.copy(armLocation = event.armLocation)
            }

            is OnNoteUpdate -> _uistate.update {
                it.copy(note = event.note)
            }

            is OnSystolicChange -> _uistate.update {
                it.copy(systolic = event.systolic)
            }

            is OnDiastolicChange -> _uistate.update {
                it.copy(diastolic = event.diastolic)
            }
            is OnPulseChange -> _uistate.update {
                it.copy(heartRate = event.pulse)
            }
            is Submit -> submitForm()
            is OnDateSelected -> {
                if (event.date != null) {
                    _selectedDate = event.date
                    _uistate.update {
                        it.copy(
                            showDatePicker = false,
                            dateString = formatDateWithTodayYesterday(event.date)
                        )
                    }
                } else {
                    _uistate.update {
                        it.copy(
                            showDatePicker = false
                        )
                    }
                }
            }

            is OnDatePickerShow -> _uistate.update { it.copy(showDatePicker = true) }
            is OnDatePickerDismiss -> _uistate.update { it.copy(showDatePicker = false) }
            is OnTimePickerShow -> _uistate.update { it.copy(showTimePicker = true) }
            is OnTimePickerDismiss -> _uistate.update { it.copy(showTimePicker = false) }
            is OnTimeSelected -> {
                _selectedHour = event.hour
                _selectedMinute = event.minute
                _uistate.update {
                    it.copy(
                        timeString = "${_selectedHour}:${_selectedMinute}",
                        showTimePicker = false
                    )
                }
            }
        }
    }

    fun submitForm() {
        viewModelScope.launch {
            addDataUseCase.invoke(
                _uistate.value.copy(
                    createdAt = updateDateTimeWithHourMinute(
                        _selectedDate,
                        _selectedHour,
                        _selectedMinute
                    )
                )
                    .toBloodPressureMeasurement()
            )
        }
    }
}

sealed interface AddDataFormEvent {
    data class OnArmLocationChange(val armLocation: Int) : AddDataFormEvent
    data class OnNoteUpdate(val note: String) : AddDataFormEvent
    data class OnSystolicChange(val systolic: Int) : AddDataFormEvent
    data class OnDiastolicChange(val diastolic: Int) : AddDataFormEvent
    data class OnPulseChange(val pulse: Int) : AddDataFormEvent
    object OnDatePickerShow : AddDataFormEvent
    object OnDatePickerDismiss : AddDataFormEvent
    data class OnDateSelected(val date: Long?) : AddDataFormEvent
    object OnTimePickerShow : AddDataFormEvent
    object OnTimePickerDismiss : AddDataFormEvent
    data class OnTimeSelected(val hour: Int, val minute: Int) : AddDataFormEvent
    data object Submit : AddDataFormEvent
    // data class OnHeartRateChange(val diastolic: Int) : AddDataFormEvent
}