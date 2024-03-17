package com.example.inhabitnow.domain.strategy

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.task.content.BaseTaskContentEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class UpdateTaskContentStrategy {
    inline operator fun invoke(
        getTaskContent: () -> BaseTaskContentEntity?,
        saveTaskContent: (startDate: LocalDate) -> ResultModel<Unit>,
        updateTaskContent: (contentId: String) -> ResultModel<Unit>,
    ): ResultModel<Unit> = getTaskContent()?.let { contentEntity ->
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val shouldUpdate = todayDate <= contentEntity.startDate
        if (shouldUpdate) updateTaskContent(contentEntity.id)
        else saveTaskContent(todayDate)
    } ?: ResultModel.Error(IllegalStateException())

}