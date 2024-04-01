package com.example.inhabitnow.android.presentation.view_activities.model

sealed interface TaskSort {
    data object ByStartDate : Common
    data object ByPriority : Common
    data object ByTitle : Common

    sealed interface Common : HabitsSort, TasksSort
    sealed interface HabitsSort : TaskSort
    sealed interface TasksSort : TaskSort

    companion object {
        private val allSorts
            get() = listOf(TaskSort.ByStartDate, TaskSort.ByPriority, TaskSort.ByTitle)

        val allHabitsSorts
            get() = allSorts.filterIsInstance<TaskSort.HabitsSort>()
    }
}