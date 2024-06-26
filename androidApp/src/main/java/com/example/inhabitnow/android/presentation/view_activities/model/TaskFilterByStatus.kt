package com.example.inhabitnow.android.presentation.view_activities.model

sealed interface TaskFilterByStatus {
    data object OnlyActive : HabitStatus, TaskStatus
    data object OnlyArchived : HabitStatus, TaskStatus

    sealed interface HabitStatus : TaskFilterByStatus
    sealed interface TaskStatus : TaskFilterByStatus

    companion object {
        private val allFilters: List<TaskFilterByStatus>
            get() = listOf(OnlyActive, OnlyArchived)

        val allHabitFilters: List<TaskFilterByStatus.HabitStatus>
            get() = allFilters.filterIsInstance<TaskFilterByStatus.HabitStatus>()

        val allTasksFilters: List<TaskStatus>
            get() = allFilters.filterIsInstance<TaskStatus>()
    }
}