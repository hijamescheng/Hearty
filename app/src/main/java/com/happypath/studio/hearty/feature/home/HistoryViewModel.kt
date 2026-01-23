package com.happypath.studio.hearty.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.GetAvgDailyMeasurementUseCase
import com.happypath.studio.hearty.domain.GetAvgWeeklyMeasurementUseCase
import com.happypath.studio.hearty.domain.GetAvgMonthlyMeasurementUseCase
import com.happypath.studio.hearty.domain.GetAvgYearlyMeasurementUseCase
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import com.happypath.studio.hearty.feature.home.MeasurementScope.DAY
import com.happypath.studio.hearty.feature.home.MeasurementScope.MONTH
import com.happypath.studio.hearty.feature.home.MeasurementScope.WEEK
import com.happypath.studio.hearty.feature.home.MeasurementScope.YEAR
import com.happypath.studio.hearty.util.DateRange
import com.happypath.studio.hearty.util.getCurrentMonthRange
import com.happypath.studio.hearty.util.getCurrentWeekRange
import com.happypath.studio.hearty.util.getCurrentYearRange
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
class HistoryViewModel @Inject constructor(
    val weeklyUseCase: GetAvgWeeklyMeasurementUseCase,
    val monthlyUseCase: GetAvgMonthlyMeasurementUseCase,
    val yearlyUseCase: GetAvgYearlyMeasurementUseCase,
    val dailyUseCase: GetAvgDailyMeasurementUseCase
) :
    ViewModel() {

    private val _weekRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentWeekRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val weekTabUiState: StateFlow<HomeTabPageUiState> = _weekRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, WEEK)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeTabPageUiState(isLoading = true)
    )
    private val _monthRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentMonthRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthTabUiState: StateFlow<HomeTabPageUiState> = _monthRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, MONTH)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeTabPageUiState(isLoading = true)
    )
    private val _yearRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentYearRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val yearTabUiState: StateFlow<HomeTabPageUiState> = _yearRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, YEAR)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeTabPageUiState(isLoading = true)
    )

    fun getMeasurementList(
        start: Long,
        end: Long,
        scope: MeasurementScope
    ): Flow<HomeTabPageUiState> {
        return flow {
            val result = when (scope) {
                WEEK -> weeklyUseCase(start, end)
                MONTH -> monthlyUseCase(start, end)
                YEAR -> yearlyUseCase(start, end)
                DAY -> dailyUseCase(start, end)
            }
            emitAll(result)
        }.map {
            getHomePageUiState(it, scope)
        }.catch {
            emit(HomeTabPageUiState(error = "Error fetching data"))
        }
    }

    fun getHomePageUiState(
        result: Result<List<MeasurementQueryResult>>,
        scope: MeasurementScope
    ): HomeTabPageUiState {
        val pair = when (scope) {
            WEEK, DAY -> _weekRangeState.value.first.toDateString("dd") + " - " + _weekRangeState.value.second.toDateString(
                "dd MMM"
            ) to !isCurrentDateWithin(_weekRangeState.value.first, _weekRangeState.value.second)

            MONTH -> _monthRangeState.value.first.toDateString("MMMM yyyy") to !isCurrentDateWithin(
                _monthRangeState.value.first,
                _monthRangeState.value.second
            )

            YEAR -> _yearRangeState.value.first.toDateString("yyyy") to !isCurrentDateWithin(
                _yearRangeState.value.first,
                _yearRangeState.value.second
            )
        }

        val list = result.getOrNull()?.filter { it.avg_dia > 0 }?.map { it.toAvgMeasurement(scope) }
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

    fun onDateRangeChanged(isPrevious: Boolean, scope: MeasurementScope) {
        val range = if (isPrevious) DateRange.PREVIOUS else DateRange.NEXT
        when (scope) {
            WEEK -> _weekRangeState.value = getCurrentWeekRange(range, _weekRangeState.value.first)
            MONTH -> _monthRangeState.value =
                getCurrentMonthRange(range, _monthRangeState.value.first)

            YEAR, DAY -> _yearRangeState.value =
                getCurrentYearRange(range, _yearRangeState.value.first)
        }
    }
}

fun MeasurementQueryResult.toAvgMeasurement(scope: MeasurementScope): AvgMeasurement {
    val dateText = when (scope) {
        DAY, WEEK -> startDate.toDateString("EEE, dd MMM")
        MONTH -> startDate.toDateString("dd") + "-" + endDate.toDateString("dd MMM yyyy")
        YEAR -> startDate.toDateString("MMM yyyy")
    }
    return AvgMeasurement(
        dateText = dateText,
        avgSys = avg_sys.toString(),
        avgDia = avg_dia.toString(),
        avgPulse = avg_pulse.toString()
    )
}

data class HomeTabPageUiState(
    val list: List<AvgMeasurement> = emptyList(),
    val dateRangeText: String = "",
    val isNextButtonEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)

data class AvgMeasurement(
    val dateText: String,
    val avgSys: String,
    val avgDia: String,
    val avgPulse: String
)

enum class MeasurementScope {
    DAY, WEEK, MONTH, YEAR
}