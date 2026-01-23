package com.happypath.studio.hearty.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.GetAvgDailyMeasurementUseCase
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import com.happypath.studio.hearty.util.DateRange
import com.happypath.studio.hearty.util.getStartAndEndOfToday
import com.happypath.studio.hearty.util.isCurrentDateWithin
import com.happypath.studio.hearty.util.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val dailyUseCase: GetAvgDailyMeasurementUseCase) :
    ViewModel() {
    private val _dayRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getStartAndEndOfToday())

    @OptIn(ExperimentalCoroutinesApi::class)
    val dayTabUiState: StateFlow<HomeTabPageUiState> = _dayRangeState.flatMapLatest {
        getDailyMeasurementSummary(it.first, it.second)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeTabPageUiState(isLoading = true)
    )

    fun getDailyMeasurementSummary(start: Long, end: Long): Flow<HomeTabPageUiState> {
        return flow {
            val result = dailyUseCase(start, end)
            emitAll(result)
        }.map {
            getDailySummaryUiState(it)
        }.catch {
            emit(HomeTabPageUiState(error = "Error fetching data"))
        }
    }

    fun getDailySummaryUiState(result: Result<List<MeasurementQueryResult>>): HomeTabPageUiState {
        val pair = _dayRangeState.value.first.toDateString("EEE, dd MMM") to !isCurrentDateWithin(
            _dayRangeState.value.first,
            _dayRangeState.value.second
        )

        val list = result.getOrNull()?.filter { it.avg_dia > 0 }?.map {
            it.toAvgMeasurement(
                MeasurementScope.DAY
            )
        }
            .orEmpty()
        return when {
            result.isSuccess -> HomeTabPageUiState(
                list = list,
                isEmpty = list.isEmpty(),
                dateRangeText = pair.first,
                isNextButtonEnabled = pair.second
            )

            else -> HomeTabPageUiState(error = "Error fetching data")
        }
    }

    fun onDateRangeChanged(isPrevious: Boolean) {
        val range = if (isPrevious) DateRange.PREVIOUS else DateRange.NEXT
        _dayRangeState.value = getStartAndEndOfToday(range, _dayRangeState.value.first)
    }
}