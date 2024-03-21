package com.example.inhabitnow.android.presentation.view_tags.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

sealed interface ViewTagsScreenNavigation : ScreenNavigation {
    data object Back : ViewTagsScreenNavigation
}