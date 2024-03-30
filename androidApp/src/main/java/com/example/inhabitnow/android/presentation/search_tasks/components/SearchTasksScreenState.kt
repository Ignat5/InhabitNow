package com.example.inhabitnow.android.presentation.search_tasks.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskModel

data class SearchTasksScreenState(
    val searchQuery: String,
    val allTasksWithContent: List<TaskModel>,
    val canClearSearch: Boolean
) : ScreenState
