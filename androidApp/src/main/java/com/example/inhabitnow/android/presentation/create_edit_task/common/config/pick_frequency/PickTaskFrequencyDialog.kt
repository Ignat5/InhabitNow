package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_frequency.components.PickTaskFrequencyScreenState
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.ui.base.BaseDaysOfWeekInput
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseItemOptionBuilder
import com.example.inhabitnow.android.ui.toDisplay
import kotlinx.datetime.DayOfWeek

@Composable
fun PickTaskFrequencyDialog(
    stateHolder: PickTaskFrequencyStateHolder,
    onResult: (PickTaskFrequencyScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskFrequencyDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskFrequencyDialogStateless(
    state: PickTaskFrequencyScreenState,
    onEvent: (PickTaskFrequencyScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseStaticDialog(
        onDismissRequest = {
            onEvent(PickTaskFrequencyScreenEvent.OnDismissRequest)
        },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Frequency")
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(PickTaskFrequencyScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskFrequencyScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val onItemClick = remember {
            val callback: (UITaskContent.Frequency.Type) -> Unit = {
                onEvent(PickTaskFrequencyScreenEvent.OnFrequencyTypeClick(it))
            }
            callback
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(
                items = UITaskContent.Frequency.Type.entries,
                key = { it.ordinal }
            ) { item ->
                val isSelected = remember(state.uiFrequencyContent.type) {
                    item == state.uiFrequencyContent.type
                }
                when (item) {
                    UITaskContent.Frequency.Type.EveryDay -> {
                        ItemFrequency(
                            title = "Every day",
                            isSelected = isSelected,
                            onClick = {
                                onItemClick(item)
                            }
                        )
                    }

                    UITaskContent.Frequency.Type.DaysOfWeek -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ItemFrequency(
                                title = "Days of week",
                                isSelected = isSelected,
                                onClick = {
                                    onItemClick(item)
                                }
                            )
                            if (isSelected) {
                                (state.uiFrequencyContent as? UITaskContent.Frequency.DaysOfWeek)?.let { fc ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BaseDaysOfWeekInput(
                                        selectedDaysOfWeek = fc.daysOfWeek,
                                        onDayOfWeekClick = {
                                            onEvent(PickTaskFrequencyScreenEvent.OnDayOfWeekClick(it))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemFrequency(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    BaseItemOptionBuilder.BaseItemRadioButton(
        titleText = title,
        isSelected = isSelected,
        onClick = onClick
    )
//    Box(
//        modifier = Modifier
//            .clickable { onClick() }
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            RadioButton(selected = isSelected, onClick = null)
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = title,
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.weight(1f)
//            )
//        }
//    }
}

//@Composable
//private fun DaysOfWeekRow(
//    selectedDaysOfWeek: Set<DayOfWeek>,
//    onDayOfWeekClick: (DayOfWeek) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val allDaysOfWeek = remember { DayOfWeek.entries }
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(allDaysOfWeek.size),
//        modifier = modifier.height(48.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        items(allDaysOfWeek) { item ->
//            val isSelected = remember(selectedDaysOfWeek) {
//                item in selectedDaysOfWeek
//            }
//            ItemDayOfWeek(
//                dayOfWeek = item,
//                isSelected = isSelected,
//                onClick = {
//                    onDayOfWeekClick(item)
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
//}
//
//@Composable
//private fun ItemDayOfWeek(
//    dayOfWeek: DayOfWeek,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val letter = remember(dayOfWeek) {
//        dayOfWeek.toDisplay().take(1)
//    }
//    Box(
//        modifier = modifier
//            .clip(MaterialTheme.shapes.small)
//            .background(
//                if (isSelected) MaterialTheme.colorScheme.primary
//                else MaterialTheme.colorScheme.surfaceVariant
//            )
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = letter,
//            style = MaterialTheme.typography.labelLarge,
//            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
//            else MaterialTheme.colorScheme.onSurfaceVariant,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//    }
//}