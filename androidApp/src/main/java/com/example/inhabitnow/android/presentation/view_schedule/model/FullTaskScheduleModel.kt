package com.example.inhabitnow.android.presentation.view_schedule.model

import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

data class FullTaskScheduleModel(
    val fullTaskModel: FullTaskModel,
    val status: TaskScheduleStatus
)
