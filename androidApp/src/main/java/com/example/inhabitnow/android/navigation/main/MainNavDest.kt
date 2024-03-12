package com.example.inhabitnow.android.navigation.main

private const val ALL_SCHEDULED_TASKS_ROUTE = "ALL_SCHEDULED_TASKS_ROUTE"
private const val VIEW_ALL_HABITS_ROUTE = "VIEW_ALL_HABITS_ROUTE"
private const val VIEW_ALL_TASKS_ROUTE = "VIEW_ALL_TASKS_ROUTE"

sealed class MainNavDest(val route: String) {
    data object AllScheduledTasksDestination : MainNavDest(ALL_SCHEDULED_TASKS_ROUTE)
    data object ViewAllHabitsDestination : MainNavDest(VIEW_ALL_HABITS_ROUTE)
    data object ViewAllTasksDestination : MainNavDest(VIEW_ALL_TASKS_ROUTE)

    companion object {
        val allDestinations: List<MainNavDest>
            get() = listOf(
                AllScheduledTasksDestination,
                ViewAllHabitsDestination,
                ViewAllTasksDestination
            )
        val startDestination
            get() = AllScheduledTasksDestination
    }
}