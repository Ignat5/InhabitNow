package com.example.inhabitnow.domain.model.statistics

import kotlinx.datetime.LocalDate

data class TaskStatisticsModel(
    val habitScore: Float,
    val currentStreak: Int,
    val bestStreak: Int,
    val currentWeekCompletionCount: Int,
    val currentMonthCompletionCount: Int,
    val currentYearCompletionCount: Int,
    val allTimeCompletionCount: Int,
    val statusMap: Map<LocalDate, TaskStatus>
)
