package com.example.inhabitnow.android.navigation

sealed class AppNavDest(val route: String) {
    data object RootGraphDestination : AppNavDest(ROOT_GRAPH_ROUTE)
    data object MainGraphDestination : AppNavDest(MAIN_GRAPH_ROUTE)
    data object CreateTaskDestination : AppNavDest("$CREATE_TASK_ROUTE/{$TASK_ID_KEY}")
    data object ViewTaskRemindersDestination : AppNavDest(
        "$VIEW_TASK_REMINDERS_ROUTE/{$TASK_ID_KEY}"
    )

    data object ViewTagsDestination : AppNavDest(VIEW_TAGS_ROUTE)
    data object SearchTasksDestination : AppNavDest(SEARCH_TASKS_ROUTE)
    data object EditTaskDestination : AppNavDest("$EDIT_TASK_ROUTE/{$TASK_ID_KEY}")
    data object ViewStatisticsDestination : AppNavDest("$VIEW_STATISTICS_ROUTE/{$TASK_ID_KEY}")

    companion object {
        private const val ROOT_GRAPH_ROUTE = "ROOT_GRAPH_ROUTE"
        private const val MAIN_GRAPH_ROUTE = "MAIN_GRAPH_ROUTE"
        private const val CREATE_TASK_ROUTE = "CREATE_TASK_ROUTE"
        private const val VIEW_TASK_REMINDERS_ROUTE = "VIEW_TASK_REMINDERS_ROUTE"
        private const val VIEW_TAGS_ROUTE = "VIEW_TAGS_ROUTE"
        private const val SEARCH_TASKS_ROUTE = "SEARCH_TASKS_ROUTE"
        private const val EDIT_TASK_ROUTE = "EDIT_TASK_ROUTE"
        private const val VIEW_STATISTICS_ROUTE = "VIEW_STATISTICS_ROUTE"

        const val TASK_ID_KEY = "TASK_ID_KEY"
        fun buildCreateTaskRoute(taskId: String) = "$CREATE_TASK_ROUTE/$taskId"
        fun buildViewTaskRemindersRoute(taskId: String) = "$VIEW_TASK_REMINDERS_ROUTE/$taskId"
        fun buildViewTagsRoute() = VIEW_TAGS_ROUTE
        fun buildSearchTasksRoute() = SEARCH_TASKS_ROUTE
        fun buildEditTaskRoute(taskId: String) = "$EDIT_TASK_ROUTE/$taskId"
        fun buildViewStatisticsRoute(taskId: String) = "$VIEW_STATISTICS_ROUTE/$taskId"
    }

}