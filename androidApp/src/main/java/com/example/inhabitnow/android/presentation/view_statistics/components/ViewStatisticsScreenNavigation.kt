package com.example.inhabitnow.android.presentation.view_statistics.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface ViewStatisticsScreenNavigation : ScreenNavigation {
    data object Back : ViewStatisticsScreenNavigation
}