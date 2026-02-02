package com.happypath.studio.hearty.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.GetProfileUseCase
import com.happypath.studio.hearty.domain.ProfileInfo
import com.happypath.studio.hearty.domain.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _profileEditState = MutableStateFlow(ProfileInfo())
    val profileEditState: StateFlow<ProfileInfo> = getProfileUseCase()
        .onEach { initialData ->
            val info = initialData.getOrNull() ?: ProfileInfo()
            _profileEditState.update { info }
        }
        .flatMapLatest { _profileEditState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileInfo()
        )

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Save -> saveProfile()
            is ProfileEvent.HeightChanged -> _profileEditState.update { it.copy(height = event.height) }
            is ProfileEvent.WeightChanged -> _profileEditState.update { it.copy(weight = event.weight) }
            is ProfileEvent.BirthdayChanged -> _profileEditState.update { it.copy(birthday = event.birthday) }
            is ProfileEvent.SexChanged -> _profileEditState.update { it.copy(sex = event.sex) }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            updateProfileUseCase(_profileEditState.value)
        }
    }

    sealed class ProfileEvent {
        object Save : ProfileEvent()
        data class HeightChanged(val height: Int) : ProfileEvent()
        data class WeightChanged(val weight: Int) : ProfileEvent()
        data class BirthdayChanged(val birthday: Long) : ProfileEvent()
        data class SexChanged(val sex: Int) : ProfileEvent()
    }
}