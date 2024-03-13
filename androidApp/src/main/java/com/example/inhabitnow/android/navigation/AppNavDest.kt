package com.example.inhabitnow.android.navigation

sealed class AppNavDest(val route: String) {

    data object RootGraphDestination : AppNavDest(ROOT_GRAPH_ROUTE)
    data object MainGraphDestination : AppNavDest(MAIN_GRAPH_ROUTE)
    data object CreateTaskDestination : AppNavDest("$CREATE_TASK_ROUTE/{$TASK_ID_KEY}")

    companion object {
        private const val ROOT_GRAPH_ROUTE = "ROOT_GRAPH_ROUTE"
        private const val MAIN_GRAPH_ROUTE = "MAIN_GRAPH_ROUTE"
        private const val CREATE_TASK_ROUTE = "CREATE_TASK_ROUTE"

        const val TASK_ID_KEY = "TASK_ID_KEY"
        fun buildCreateTaskRoute(taskId: String) = "$CREATE_TASK_ROUTE/$taskId"
    }

}