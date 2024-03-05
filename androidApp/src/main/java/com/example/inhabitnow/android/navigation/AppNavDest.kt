package com.example.inhabitnow.android.navigation

private const val ROOT_GRAPH_ROUTE = "ROOT_GRAPH_ROUTE"

sealed class AppNavDest(val route: String) {

    data object RootGraphDestination : AppNavDest(ROOT_GRAPH_ROUTE)

}