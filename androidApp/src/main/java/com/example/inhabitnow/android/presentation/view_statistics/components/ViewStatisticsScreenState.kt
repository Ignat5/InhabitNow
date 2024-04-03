package com.example.inhabitnow.android.presentation.view_statistics.components

import androidx.compose.runtime.Stable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.view_statistics.model.UIStatisticsModel
import com.example.inhabitnow.domain.model.task.TaskModel

@Stable
data class ViewStatisticsScreenState(
    val habitModel: TaskModel.Habit?,
    val uiStatisticsModel: UIStatisticsModel
) : ScreenState
