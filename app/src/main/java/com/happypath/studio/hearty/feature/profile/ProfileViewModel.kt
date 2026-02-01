package com.happypath.studio.hearty.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.GetProfileUseCase
import com.happypath.studio.hearty.domain.ProfileInfo
import com.happypath.studio.hearty.domain.UpdateProfileUseCase
import com.happypath.studio.hearty.util.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private var _profileInfo = MutableStateFlow(ProfileInfo(0, 0, 0, 0))
    val profileEditState: StateFlow<ProfileInfo> = _profileInfo

    @OptIn(ExperimentalCoroutinesApi::class)
    val profileState: StateFlow<ProfileState> = getProfileUseCase()
        .map { result ->
            val info = result.getOrNull()
            info?.toProfileState() ?: ProfileState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileState()
        )

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Save -> saveProfile()
            is ProfileEvent.HeightChanged -> {
                _profileInfo.update { it.copy(height = event.height) }
            }

            is ProfileEvent.WeightChanged -> {
                _profileInfo.update { it.copy(weight = event.weight) }
            }

            is ProfileEvent.BirthdayChanged -> {
                _profileInfo.update { it.copy(birthday = event.birthday) }
            }

            is ProfileEvent.SexChanged -> {
                _profileInfo.update { it.copy(sex = event.sex) }
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            updateProfileUseCase(_profileInfo.value)
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

data class ProfileState(
    val heightText: String = "N/A",
    val weightText: String = "N/A",
    val birthdayText: String = "N/A",
    val sexText: String = "N/A"
)

fun ProfileInfo.toProfileState() = ProfileState(
    heightText = "$height cm",
    weightText = "$weight kg",
    birthdayText = birthday.toDateString("dd MMM yyyy"),
    sexText = if (sex == 1) "Male" else "Female"
)