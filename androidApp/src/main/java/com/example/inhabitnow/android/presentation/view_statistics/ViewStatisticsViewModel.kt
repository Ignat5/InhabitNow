package com.example.inhabitnow.android.presentation.view_statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inhabitnow.android.navigation.AppNavDest
import com.example.inhabitnow.android.presentation.base.view_model.BaseViewModel
import com.example.inhabitnow.android.presentation.view_activities.base.BaseViewTasksViewModel
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenConfig
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenEvent
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenNavigation
import com.example.inhabitnow.android.presentation.view_statistics.components.ViewStatisticsScreenState
import com.example.inhabitnow.android.presentation.view_statistics.model.UICompletionModel
import com.example.inhabitnow.android.presentation.view_statistics.model.UIStatisticsModel
import com.example.inhabitnow.android.presentation.view_statistics.model.UIStreakModel
import com.example.inhabitnow.domain.model.statistics.TaskStatisticsModel
import com.example.inhabitnow.domain.model.task.TaskModel
import com.example.inhabitnow.domain.use_case.calculate_statistics.CalculateStatisticsUseCase
import com.example.inhabitnow.domain.use_case.read_task_with_content_by_id.ReadTaskWithContentByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewStatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    calculateStatisticsUseCase: CalculateStatisticsUseCase,
    readTaskWithContentByIdUseCase: ReadTaskWithContentByIdUseCase
) : BaseViewModel<ViewStatisticsScreenEvent, ViewStatisticsScreenState, ViewStatisticsScreenNavigation, ViewStatisticsScreenConfig>() {
    private val taskId: String = checkNotNull(savedStateHandle.get<String>(AppNavDest.TASK_ID_KEY))
    private val habitModelState = readTaskWithContentByIdUseCase(taskId)
        .filterIsInstance<TaskModel.Habit>()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val statisticsState = flow<UIStatisticsModel> {
        calculateStatisticsUseCase(taskId)?.let { taskStatisticsModel ->
            emit(taskStatisticsModel.toUIStatisticsModel())
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        UIStatisticsModel(
            habitScorePercent = DEFAULT_VALUE,
            streakModel = UIStreakModel(
                currentStreak = DEFAULT_VALUE,
                bestStreak = DEFAULT_VALUE
            ),
            completionModel = UICompletionModel(
                currentWeekCompletionCount = DEFAULT_VALUE,
                currentMonthCompletionCount = DEFAULT_VALUE,
                currentYearCompletionCount = DEFAULT_VALUE,
                allTimeCompletionCount = DEFAULT_VALUE,
            )
        )
    )

    override val uiScreenState: StateFlow<ViewStatisticsScreenState> = combine(
        habitModelState,
        statisticsState
    ) { habitModel, statistics ->
        ViewStatisticsScreenState(
            habitModel = habitModel,
            uiStatisticsModel = statistics
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ViewStatisticsScreenState(
            habitModel = habitModelState.value,
            uiStatisticsModel = statisticsState.value
        )
    )

    override fun onEvent(event: ViewStatisticsScreenEvent) {
        when (event) {
            is ViewStatisticsScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onDismissRequest() {
        setUpNavigationState(ViewStatisticsScreenNavigation.Back)
    }

    private fun TaskStatisticsModel.toUIStatisticsModel() = UIStatisticsModel(
        habitScorePercent = (habitScore * 100).toInt(),
        streakModel = UIStreakModel(
            currentStreak = currentStreak,
            bestStreak = bestStreak
        ),
        completionModel = UICompletionModel(
            currentWeekCompletionCount = currentWeekCompletionCount,
            currentMonthCompletionCount = currentMonthCompletionCount,
            currentYearCompletionCount = currentYearCompletionCount,
            allTimeCompletionCount = allTimeCompletionCount
        )
    )

    companion object {
        private const val DEFAULT_VALUE = 0
    }

}