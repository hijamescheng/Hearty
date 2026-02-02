package com.happypath.studio.hearty.feature.profile

import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val profileState: StateFlow<ProfileState> = getProfileUseCase()
        .map { result ->
            result.getOrNull()?.toProfileState() ?: ProfileState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileState()
        )
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
    sexText = when (sex) {
        0 -> "Female"
        1 -> "Male"
        else -> "Other"
    }
)