package com.example.inhabitnow.android.presentation.create_edit_task.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.base.BaseCreateEditTaskBuilder
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenNavigation
import com.example.inhabitnow.android.presentation.create_edit_task.edit.components.EditTaskScreenState
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskDialog
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskDialog
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_restart.ConfirmRestartHabitDialog
import com.example.inhabitnow.android.presentation.create_edit_task.edit.model.ItemTaskAction

@Composable
fun EditTaskScreen(onNavigate: (EditTaskScreenNavigation) -> Unit) {
    val viewModel: EditTaskViewModel = hiltViewModel()
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigate,
        configContent = { config, onEvent ->
            EditTaskScreenConfigStateless(config, onEvent)
        }
    ) { state, onEvent ->
        EditTaskScreenStateless(state, onEvent)
    }
}

@Composable
private fun EditTaskScreenStateless(
    state: EditTaskScreenState,
    onEvent: (EditTaskScreenEvent) -> Unit
) {
    BackHandler { onEvent(EditTaskScreenEvent.OnBackRequest) }
    Scaffold(
        topBar = {
            EditTaskTopBar(
                allTaskActionItems = state.allTaskActionItems,
                onItemTaskActionClick = {
                    onEvent(EditTaskScreenEvent.OnItemTaskActionClick(it))
                },
                onBackClick = {
                    onEvent(EditTaskScreenEvent.OnBackRequest)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                BaseCreateEditTaskBuilder.baseConfigItems(
                    lazyListScope = this,
                    allTaskConfigItems = state.allTaskConfigItems,
                    onItemClick = { item ->
                        onEvent(
                            EditTaskScreenEvent.BaseEvent(
                                BaseCreateEditTaskScreenEvent.OnBaseItemTaskConfigClick(item)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EditTaskScreenConfigStateless(
    config: EditTaskScreenConfig,
    onResultEvent: (EditTaskScreenEvent) -> Unit
) {
    when (config) {
        is EditTaskScreenConfig.BaseConfig -> {
            BaseCreateEditTaskBuilder.BaseScreenConfig(
                baseConfig = config.baseConfig,
                onBaseResultEvent = {
                    onResultEvent(EditTaskScreenEvent.BaseEvent(it))
                }
            )
        }

        is EditTaskScreenConfig.ConfirmArchiveTask -> {
            ConfirmArchiveTaskDialog(
                taskId = config.taskId,
                onResult = {
                    onResultEvent(
                        EditTaskScreenEvent.ResultEvent.ConfirmArchiveTask(it)
                    )
                }
            )
        }

        is EditTaskScreenConfig.ConfirmDeleteTask -> {
            ConfirmDeleteTaskDialog(
                taskId = config.taskId,
                onResult = {
                    onResultEvent(EditTaskScreenEvent.ResultEvent.ConfirmDeleteTask(it))
                }
            )
        }

        is EditTaskScreenConfig.ConfirmRestartHabit -> {
            ConfirmRestartHabitDialog(
                onResult = {
                    onResultEvent(EditTaskScreenEvent.ResultEvent.ConfirmRestartHabit(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskTopBar(
    allTaskActionItems: List<ItemTaskAction>,
    onItemTaskActionClick: (ItemTaskAction) -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Edit activity")
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            var isMenuExpanded by remember {
                mutableStateOf(false)
            }
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(painter = painterResource(id = R.drawable.ic_more), contentDescription = null)
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    val all = remember { allTaskActionItems }
                    all.forEach { itemTaskAction ->
                        val actionText = remember {
                            when (itemTaskAction) {
                                is ItemTaskAction.ViewStatistics -> "View statistics"
                                is ItemTaskAction.RestartHabit -> "Restart habit"
                                is ItemTaskAction.ArchiveUnarchive -> {
                                    when (itemTaskAction) {
                                        is ItemTaskAction.ArchiveUnarchive.Archive -> "Archive"
                                        is ItemTaskAction.ArchiveUnarchive.Unarchive -> "Unarchive"
                                    }
                                }

                                is ItemTaskAction.DeleteTask -> "Delete"
                            }
                        }
                        DropdownMenuItem(
                            text = {
                                Text(text = actionText)
                            },
                            leadingIcon = {
                                val iconResId = remember {
                                    when (itemTaskAction) {
                                        is ItemTaskAction.ViewStatistics -> R.drawable.ic_statistics
                                        is ItemTaskAction.RestartHabit -> R.drawable.ic_reset
                                        is ItemTaskAction.ArchiveUnarchive -> {
                                            when (itemTaskAction) {
                                                is ItemTaskAction.ArchiveUnarchive.Archive -> R.drawable.ic_archive
                                                is ItemTaskAction.ArchiveUnarchive.Unarchive -> R.drawable.ic_unarchive
                                            }
                                        }

                                        is ItemTaskAction.DeleteTask -> R.drawable.ic_delete
                                    }
                                }
                                Icon(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onItemTaskActionClick(itemTaskAction)
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}