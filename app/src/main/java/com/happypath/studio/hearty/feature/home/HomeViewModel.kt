package com.happypath.studio.hearty.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happypath.studio.hearty.domain.GetAvgMeasurementBetweenUseCase
import com.happypath.studio.hearty.domain.MeasurementQueryResult
import com.happypath.studio.hearty.feature.home.MeasurementScope.DAY
import com.happypath.studio.hearty.feature.home.MeasurementScope.MONTH
import com.happypath.studio.hearty.feature.home.MeasurementScope.WEEK
import com.happypath.studio.hearty.feature.home.MeasurementScope.YEAR
import com.happypath.studio.hearty.util.getCurrentMonthRange
import com.happypath.studio.hearty.util.getCurrentWeekRange
import com.happypath.studio.hearty.util.getCurrentYearRange
import com.happypath.studio.hearty.util.getStartAndEndOfToday
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
class HomeViewModel @Inject constructor(val usecase: GetAvgMeasurementBetweenUseCase) :
    ViewModel() {
    private val _dayRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getStartAndEndOfToday())

    @OptIn(ExperimentalCoroutinesApi::class)
    val dayTabUiState: StateFlow<HomePageUiState> = _dayRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, DAY)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomePageUiState(isLoading = true)
    )
    private val _weekRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentWeekRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val weekTabUiState: StateFlow<HomePageUiState> = _weekRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, WEEK)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomePageUiState(isLoading = true)
    )
    private val _monthRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentMonthRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthTabUiState: StateFlow<HomePageUiState> = _monthRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, MONTH)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomePageUiState(isLoading = true)
    )
    private val _yearRangeState: MutableStateFlow<Pair<Long, Long>> =
        MutableStateFlow(getCurrentYearRange())

    @OptIn(ExperimentalCoroutinesApi::class)
    val yearTabUiState: StateFlow<HomePageUiState> = _yearRangeState.flatMapLatest {
        getMeasurementList(it.first, it.second, YEAR)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  HomePageUiState(isLoading = true)
    )

    fun getMeasurementList(start: Long, end: Long, scope: MeasurementScope): Flow<HomePageUiState> {
        return flow {
            val result = usecase(start, end)
            emitAll(result)
        }.map {
            if (it.isSuccess && !it.getOrNull().isNullOrEmpty()) {
                getHomePageUiState(it, scope)
            } else {
                HomePageUiState(error = "Error fetching data")
            }
        }.catch {
            emit(HomePageUiState(error = "Error fetching data"))
        }
    }


    fun getHomePageUiState(
        result: Result<List<MeasurementQueryResult>>,
        scope: MeasurementScope
    ): HomePageUiState {
        val dateText = when (scope) {
            DAY -> _dayRangeState.value.first.toDateString("EEE, dd MMM")
            WEEK -> _weekRangeState.value.first.toDateString("dd") + " - " + _weekRangeState.value.second.toDateString("dd MMM")
            MONTH -> _monthRangeState.value.first.toDateString("MMMM yyyy")
            YEAR -> _yearRangeState.value.first.toDateString("yyyy")
        }
        return when {
            result.isSuccess -> HomePageUiState(
                result.getOrNull()?.map { it.toAvgMeasurement() }.orEmpty(),
                dateText
            )

            else -> HomePageUiState(error = "Error fetching data")
        }
    }
}

fun MeasurementQueryResult.toAvgMeasurement(): AvgMeasurement {
    return AvgMeasurement(
        dateText = date.toDateString("EEE, dd MMM"),
        avgSys = avg_sys.toString(),
        avgDia = avg_dia.toString(),
        avgPulse = avg_pulse.toString()
    )
}

data class HomePageUiState(
    val list: List<AvgMeasurement> = emptyList(),
    val dateRangeText: String = "",
    val isLoading: Boolean = false,
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