package com.example.inhabitnow.domain.use_case.calculate_statistics

import com.example.inhabitnow.domain.model.statistics.TaskStatisticsModel

interface CalculateStatisticsUseCase {
    suspend operator fun invoke(taskId: String): TaskStatisticsModel?
}