package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation

interface CreateTaskScreenNavigation : ScreenNavigation {
    data class Base(val baseSN: BaseCreateEditTaskScreenNavigation) : CreateTaskScreenNavigation
    data object Back : CreateTaskScreenNavigation
}