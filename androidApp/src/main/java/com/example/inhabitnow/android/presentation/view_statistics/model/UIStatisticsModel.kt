package com.example.inhabitnow.android.presentation.view_statistics.model

import androidx.compose.runtime.Stable

@Stable
data class UIStatisticsModel(
    val habitScorePercent: Int,
    val streakModel: UIStreakModel,
    val completionModel: UICompletionModel,
)

@Stable
data class UIStreakModel(
    val currentStreak: Int,
    val bestStreak: Int
)

@Stable
data class UICompletionModel(
    val currentWeekCompletionCount: Int,
    val currentMonthCompletionCount: Int,
    val currentYearCompletionCount: Int,
    val allTimeCompletionCount: Int
)
