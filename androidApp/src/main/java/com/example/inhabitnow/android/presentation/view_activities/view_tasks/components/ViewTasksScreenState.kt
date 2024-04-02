package com.example.inhabitnow.android.presentation.view_activities.view_tasks.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.domain.model.task.derived.FullTaskModel

data class ViewTasksScreenState(
    val allTasksResult: UIResultModel<List<FullTaskModel.FullTask>>,
    val allSelectableTags: List<SelectableTagModel>,
    val filterByStatus: TaskFilterByStatus.TaskStatus?,
    val sort: TaskSort.TasksSort?
) : ScreenState
