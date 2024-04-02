package com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.R
import com.example.inhabitnow.android.presentation.base.compose.BaseModalBottomSheetDialog
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_activities.model.ItemTaskAction
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenEvent
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitnow.android.presentation.view_activities.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenState
import com.example.inhabitnow.android.ui.base.BaseItemOptionBuilder

@Composable
fun ViewHabitActionsDialog(
    stateHolder: ViewHabitActionsStateHolder,
    onResult: (ViewHabitActionsScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ViewHabitActionsDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewHabitActionsDialogStateless(
    state: ViewHabitActionsScreenState,
    onEvent: (ViewHabitActionsScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onEvent(ViewHabitActionsScreenEvent.OnDismissRequest) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            TitleRow(
                title = state.habitModel.title,
                onEditClick = { onEvent(ViewHabitActionsScreenEvent.OnEditClick) }
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = state.allActionItems) { item ->
                    ItemAction(
                        item = item,
                        onClick = {
                            onEvent(ViewHabitActionsScreenEvent.OnItemActionClick(item))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleRow(
    title: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEditClick) {
            Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null)
        }
    }
}

@Composable
private fun ItemAction(
    item: ItemTaskAction.HabitAction,
    onClick: () -> Unit
) {
    val iconResId = remember(item) {
        when (item) {
            is ItemTaskAction.ViewStatistics -> R.drawable.ic_statistics
            is ItemTaskAction.ArchiveUnarchive -> {
                when (item) {
                    is ItemTaskAction.ArchiveUnarchive.Archive -> R.drawable.ic_archive
                    is ItemTaskAction.ArchiveUnarchive.Unarchive -> R.drawable.ic_unarchive
                }
            }

            is ItemTaskAction.Delete -> R.drawable.ic_delete
        }
    }

    val title = remember(item) {
        when (item) {
            is ItemTaskAction.ViewStatistics -> "View statistics"
            is ItemTaskAction.ArchiveUnarchive -> {
                when (item) {
                    is ItemTaskAction.ArchiveUnarchive.Archive -> "Archive"
                    is ItemTaskAction.ArchiveUnarchive.Unarchive -> "Unarchive"
                }
            }

            is ItemTaskAction.Delete -> "Delete"
        }
    }
    BaseItemOptionBuilder.BaseIconTitleOptionItem(
        iconResId = iconResId,
        title = title,
        onClick = onClick
    )
}






