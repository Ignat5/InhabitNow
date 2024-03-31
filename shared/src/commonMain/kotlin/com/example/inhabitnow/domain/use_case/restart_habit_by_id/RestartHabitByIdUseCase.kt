package com.example.inhabitnow.domain.use_case.restart_habit_by_id

import com.example.inhabitnow.core.model.ResultModel

interface RestartHabitByIdUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<Unit>
}