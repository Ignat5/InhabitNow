package com.example.inhabitnow.android.presentation.view_activities.model

sealed interface TaskFilterByStatus {
    data object OnlyActive : Common
    data object OnlyArchived : Common

    sealed interface Common : HabitStatus, TaskStatus
    sealed interface HabitStatus : TaskFilterByStatus
    sealed interface TaskStatus : TaskFilterByStatus

    companion object {
        private val allFilters: List<TaskFilterByStatus>
            get() = listOf(OnlyActive, OnlyArchived)

        val allHabitFilters: List<TaskFilterByStatus.HabitStatus>
            get() = allFilters.filterIsInstance<TaskFilterByStatus.HabitStatus>()
    }
}