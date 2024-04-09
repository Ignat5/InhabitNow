package com.example.inhabitnow.android.navigation.root

import androidx.navigation.NavOptions

sealed interface TargetNavDest {
    data class Destination(val route: String, val navOptions: NavOptions? = null) : TargetNavDest
    data object Back : TargetNavDest
}