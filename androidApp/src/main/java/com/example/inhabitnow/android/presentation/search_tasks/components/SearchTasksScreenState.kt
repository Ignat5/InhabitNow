package com.example.inhabitnow.android.presentation.search_tasks.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.domain.model.task.TaskWithContentModel

data class SearchTasksScreenState(
    val searchQuery: String,
    val allTasksWithContent: List<TaskWithContentModel>,
    val canClearSearch: Boolean
) : ScreenState
