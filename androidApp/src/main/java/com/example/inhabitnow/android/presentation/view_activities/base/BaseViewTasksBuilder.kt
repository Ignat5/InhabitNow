package com.example.inhabitnow.android.presentation.view_activities.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive.ConfirmArchiveTaskDialog
import com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete.ConfirmDeleteTaskDialog
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort

object BaseViewTasksBuilder {

    @Composable
    fun <T : TaskSort> ChipSort(
        allSort: List<T>,
        currentSort: T?,
        getTextBySort: (T) -> String,
        onSortClick: (T) -> Unit
    ) {
        BaseDropdownChip(
            allItems = allSort,
            isFilterActive = currentSort != null,
            onItemClick = { item ->
                onSortClick(item)
            },
            label = {
                val text = remember(currentSort) {
                    if (currentSort != null) {
                        getTextBySort(currentSort)
                    } else "Sort"
                }
                Text(text = text)
            },
            textContent = { item ->
                val text = remember(item) {
                    getTextBySort(item)
                }
                Text(text = text)
            },
            checkIsSelected = { item ->
                remember {
                    item == currentSort
                }
            }
        )
    }

    @Composable
    fun <T : TaskFilterByStatus> ChipFilterByStatus(
        allFilters: List<T>,
        currentFilter: T?,
        getTextByFilter: (T) -> String,
        onFilterClick: (T) -> Unit
    ) {
        BaseDropdownChip(
            allItems = allFilters,
            isFilterActive = currentFilter != null,
            onItemClick = { item ->
                onFilterClick(item)
            },
            label = {
                val text = remember(currentFilter) {
                    if (currentFilter != null) {
                        getTextByFilter(currentFilter)
                    } else "Status"
                }
                Text(text = text)
            },
            textContent = { item ->
                val text = remember(item) {
                    getTextByFilter(item)
                }
                Text(text = text)
            },
            checkIsSelected = { item ->
                remember {
                    item == currentFilter
                }
            }
        )
    }

    @Composable
    fun ChipFilterByTags(
        allSelectableTags: List<SelectableTagModel>,
        onTagClick: (tagId: String) -> Unit
    ) {
        BaseDropdownChip(
            allItems = allSelectableTags,
            isFilterActive = allSelectableTags.any { it.isSelected },
            onItemClick = {
                onTagClick(it.tagModel.id)
            },
            label = {
                Text(text = "Tag")
            },
            textContent = { item ->
                Text(text = item.tagModel.title)
            },
            checkIsSelected = { item ->
                item.isSelected
            },
            shouldCollapseOnClick = false
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun <T> BaseDropdownChip(
        allItems: List<T>,
        isFilterActive: Boolean,
        onItemClick: (T) -> Unit,
        label: @Composable () -> Unit,
        textContent: @Composable (T) -> Unit,
        checkIsSelected: @Composable (T) -> Boolean,
        shouldCollapseOnClick: Boolean = true
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Box {
            FilterChip(
                selected = isFilterActive,
                onClick = { isExpanded = !isExpanded },
                label = label,
                leadingIcon = if (isFilterActive) {
                    val icon: @Composable () -> Unit = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            modifier = Modifier.size(16.dp),
                            contentDescription = null
                        )
                    }
                    icon
                } else null,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_dropdown),
                        null,
                        Modifier.rotate(if (isExpanded) 180f else 0f)
                    )
                }
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                allItems.forEach { item ->
                    val isSelected = checkIsSelected(item)
                    DropdownMenuItem(
                        text = {
                            textContent(item)
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier
                                    .size(16.dp)
                                    .alpha(if (isSelected) 1f else 0f),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            if (shouldCollapseOnClick) {
                                isExpanded = false
                            }
                            onItemClick(item)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun BaseScreenConfig(
        config: BaseViewTasksScreenConfig,
        onResultEvent: (BaseViewTasksScreenEvent.ResultEvent) -> Unit
    ) {
        when (config) {
            is BaseViewTasksScreenConfig.ConfirmArchiveTask -> {
                ConfirmArchiveTaskDialog(config.taskId) {
                    onResultEvent(
                        BaseViewTasksScreenEvent.ResultEvent.ConfirmArchiveTask(it)
                    )
                }
            }

            is BaseViewTasksScreenConfig.ConfirmDeleteTask -> {
                ConfirmDeleteTaskDialog(config.taskId) {
                    onResultEvent(
                        BaseViewTasksScreenEvent.ResultEvent.ConfirmDeleteTask(it)
                    )
                }
            }
        }
    }

    @Composable
    fun NoDataMessage(
        boxScope: BoxScope,
        result: UIResultModel<List<*>>
    ) {
        boxScope.apply {
            val message = remember(result) {
                when (result) {
                    is UIResultModel.NoData -> {
                        "You have no activities yet"
                    }

                    is UIResultModel.Data -> {
                        if (result.data.isEmpty()) {
                            "No activities matching specified filters"
                        } else null
                    }

                    is UIResultModel.Loading -> null
                }
            }
            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }
    }
}