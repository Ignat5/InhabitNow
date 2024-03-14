package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation

interface CreateTaskScreenNavigation : ScreenNavigation {
    data object Back : CreateTaskScreenNavigation
}