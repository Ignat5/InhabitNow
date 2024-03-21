package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_tags.components.PickTaskTagsScreenState
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseItemOptionBuilder
import com.example.inhabitnow.domain.model.tag.TagModel

@Composable
fun PickTaskTagsDialog(
    stateHolder: PickTaskTagsStateHolder,
    onResult: (PickTaskTagsScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskTagsDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PickTaskTagsDialogStateless(
    state: PickTaskTagsScreenState,
    onEvent: (PickTaskTagsScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseStaticDialog(
        onDismissRequest = { onEvent(PickTaskTagsScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BaseDialogBuilder.BaseDialogTitle(
                    titleText = "Tags",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onEvent(PickTaskTagsScreenEvent.OnManageTagsClick) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onEvent(PickTaskTagsScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskTagsScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                when (val result = state.allSelectableTagsResultModel) {
                    is UIResultModel.Loading, is UIResultModel.Data -> {
                        items(
                            items = result.data ?: emptyList(),
                            key = { it.tagModel.id },
                        ) { item ->
                            val tagModel = remember(item.tagModel) {
                                item.tagModel
                            }
                            val isSelected = remember(item.isSelected) {
                                item.isSelected
                            }
                            ItemTag(
                                tagModel = tagModel,
                                isSelected = isSelected,
                                onClick = {
                                    onEvent(PickTaskTagsScreenEvent.OnTagClick(item.tagModel.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }

                    is UIResultModel.NoData -> Unit
                }
            }

            if (state.allSelectableTagsResultModel is UIResultModel.NoData) {
                Text(
                    text = "No tags",
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

@Composable
private fun ItemTag(
    tagModel: TagModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseItemOptionBuilder.BaseItemCheckbox(
        titleText = tagModel.title,
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    )
}