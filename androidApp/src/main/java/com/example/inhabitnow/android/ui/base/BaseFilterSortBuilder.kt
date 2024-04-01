package com.example.inhabitnow.android.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.model.SelectableTagModel
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.android.presentation.view_activities.model.TaskSort
import com.example.inhabitnow.domain.model.tag.TagModel

object BaseFilterSortBuilder {

    @Composable
    fun ChipHabitSort(
        currentSort: TaskSort.HabitsSort?,
        onSortClick: (TaskSort.HabitsSort) -> Unit
    ) {
        BaseChipSort(
            currentSort = currentSort,
            allSorts = TaskSort.allHabitsSorts,
            getTextBySort = { sort ->
                when (sort) {
                    is TaskSort.ByStartDate -> "By start date"
                    is TaskSort.ByPriority -> "By priority"
                    is TaskSort.ByTitle -> "By title"
                }
            },
            onSortClick = onSortClick
        )
    }

    @Composable
    private fun <T : TaskSort> BaseChipSort(
        currentSort: T?,
        allSorts: List<T>,
        getTextBySort: (T) -> String,
        onSortClick: (T) -> Unit
    ) {
        val isFilterActive = remember(currentSort) { currentSort != null }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Box {
            FilterChip(
                selected = isFilterActive,
                onClick = { isExpanded = !isExpanded },
                label = {
                    val text = remember(currentSort) {
                        if (currentSort != null) {
                            getTextBySort(currentSort)
                        } else "Sort"
                    }
                    Text(text = text)
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                        contentDescription = null
                    )
                }
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                allSorts.forEach { item ->
                    val isSelected = remember {
                        item == currentSort
                    }
                    val text = remember {
                        getTextBySort(item)
                    }
                    DropdownMenuItem(
                        text = {
                            Text(text = text)
                        },
                        onClick = {
                            isExpanded = false
                            onSortClick(item)
                        },
                        modifier = Modifier.background(
                            if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest
                            else Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun ChipFilterByHabitStatus(
        currentFilter: TaskFilterByStatus.HabitStatus?,
        onFilterClick: (TaskFilterByStatus.HabitStatus) -> Unit
    ) {
        BaseChipFilterByStatus(
            currentFilter = currentFilter,
            allFilters = TaskFilterByStatus.allHabitFilters,
            getTextByFilter = { filter ->
                when (filter) {
                    is TaskFilterByStatus.OnlyActive -> "Only active"
                    is TaskFilterByStatus.OnlyArchived -> "Only archived"
                }
            },
            onFilterClick = onFilterClick
        )
    }

    @Composable
    private fun <T : TaskFilterByStatus> BaseChipFilterByStatus(
        currentFilter: T?,
        allFilters: List<T>,
        getTextByFilter: (T) -> String,
        onFilterClick: (T) -> Unit
    ) {
        val isFilterActive = remember(currentFilter) { currentFilter != null }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Box {
            FilterChip(
                selected = isFilterActive,
                onClick = { isExpanded = !isExpanded },
                label = {
                    val text = remember(currentFilter) {
                        if (currentFilter != null) {
                            getTextByFilter(currentFilter)
                        } else "Status"
                    }
                    Text(text = text)
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                        contentDescription = null
                    )
                }
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                allFilters.forEach { item ->
                    val isSelected = remember {
                        item == currentFilter
                    }
                    val text = remember {
                        getTextByFilter(item)
                    }
                    DropdownMenuItem(
                        text = {
                            Text(text = text)
                        },
                        onClick = {
                            isExpanded = false
                            onFilterClick(item)
                        },
                        modifier = Modifier.background(
                            if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest
                            else Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun ChipFilterByTags(
        allSelectableTags: List<SelectableTagModel>,
        onTagClick: (String) -> Unit
    ) {
        val isFilterActive = remember(allSelectableTags) {
            allSelectableTags.any { it.isSelected }
        }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Box {
            FilterChip(
                selected = isFilterActive,
                onClick = {
                    isExpanded = !isExpanded
                },
                label = {
                    Text(text = "Tag")
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                        contentDescription = null
                    )
                }
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                allSelectableTags.forEach { selectableTag ->
                    key(selectableTag.tagModel.id) {
                        DropdownMenuItem(
                            modifier = Modifier.background(
                                if (selectableTag.isSelected) MaterialTheme.colorScheme.surfaceContainerHighest
                                else Color.Transparent
                            ),
                            text = {
                                ItemTag(tagModel = selectableTag.tagModel)
                            },
                            onClick = {
                                onTagClick(selectableTag.tagModel.id)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemTag(tagModel: TagModel) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tag),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tagModel.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}