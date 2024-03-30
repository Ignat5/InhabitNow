package com.example.inhabitnow.android.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.inhabitnow.android.presentation.view_activities.model.TaskFilterByStatus
import com.example.inhabitnow.domain.model.tag.TagModel

object BaseFilterSortBuilder {

    @Composable
    fun<T: TaskFilterByStatus> ChipFilterByStatus(
        currentFilter: T?,
        allFilters: List<T>,
        onFilterClick: (T) -> Unit
    ) {
        val isFilterActive = remember(currentFilter) { currentFilter != null }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        FilterChip(
            selected = isFilterActive,
            onClick = { isExpanded = !isExpanded },
            label = {
                Text(text = "Status")
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                    contentDescription = null
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
                            when (item) {
                                is TaskFilterByStatus.OnlyActive -> "Only active"
                                is TaskFilterByStatus.OnlyArchived -> "Only archived"
                                else -> ""
                            }
                        }
                        DropdownMenuItem(
                            text = {
                                Text(text = text)
                            },
                            onClick = {
                                onFilterClick(item)
                                isExpanded = false
                            },
                            modifier = Modifier.background(
                                if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest
                                else Color.Transparent
                            )
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun ChipFilterByTags(
        allTags: List<TagModel>,
        filterByTagsIds: Set<String>,
        onTagClick: (String) -> Unit
    ) {
        val isFilterActive = remember(filterByTagsIds) {
            filterByTagsIds.isNotEmpty()
        }
        var isExpanded by remember {
            mutableStateOf(false)
        }
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
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    allTags.forEach { tagModel ->
                        val isSelected = remember(filterByTagsIds) {
                            tagModel.id in filterByTagsIds
                        }
                        key(tagModel.id) {
                            DropdownMenuItem(
                                modifier = Modifier.background(
                                    if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest
                                    else Color.Transparent
                                ),
                                text = {
                                    ItemTag(tagModel = tagModel)
                                },
                                onClick = {
                                    onTagClick(tagModel.id)
                                }
                            )
                        }
                    }
                }
            }
        )
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