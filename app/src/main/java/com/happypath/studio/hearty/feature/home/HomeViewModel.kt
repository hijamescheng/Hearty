package com.happypath.studio.hearty.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.BloodPressureMeasurement
import com.happypath.studio.hearty.domain.GetMeasurementListUseCase
import com.happypath.studio.hearty.util.getStartAndEndOfToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val usecase: GetMeasurementListUseCase) : ViewModel() {

    val uistate: StateFlow<HomePageUiState> = getMeasurementList(MeasurementScope.DAY)

    fun getMeasurementList(scope: MeasurementScope): StateFlow<HomePageUiState> {
        val pair = getStartAndEndOfToday()
        return flow {
            val result = usecase(pair.first, pair.second)
            emitAll(result)
        }.map {
            getHomePageUiState(it)
        }.catch {
            emit(HomePageUiState.Error("Error fetching data"))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomePageUiState.Loading
        )
    }

    fun getHomePageUiState(result: Result<List<BloodPressureMeasurement>>): HomePageUiState {
        return when {
            result.isSuccess -> {
                result.getOrNull()?.let {
                    HomePageUiState.Success(it)
                } ?: HomePageUiState.Empty
            }

            else -> HomePageUiState.Error("Error fetching data")
        }
    }
}

sealed interface HomePageUiState {
    data class Success(
        val list: List<BloodPressureMeasurement>
    ) : HomePageUiState

    object Loading : HomePageUiState
    object Empty : HomePageUiState
    data class Error(val message: String) : HomePageUiState
}

enum class MeasurementScope {
    DAY, WEEK, MONTH, YEAR
}