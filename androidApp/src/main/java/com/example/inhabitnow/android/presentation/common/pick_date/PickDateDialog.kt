package com.example.inhabitnow.android.presentation.common.pick_date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenEvent
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenResult
import com.example.inhabitnow.android.presentation.common.pick_date.components.PickDateScreenState
import com.example.inhabitnow.android.ui.base.BaseDatePickerBuilder
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.toDayOfWeekMonthDayOfMonth

private const val DIALOG_SCREEN_FRACTION = 0.75f

@Composable
fun PickDateDialog(
    stateHolder: PickDateStateHolder,
    onResult: (PickDateScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickDateDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickDateDialogStateless(
    state: PickDateScreenState,
    onEvent: (PickDateScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseStaticDialog(
        onDismissRequest = { onEvent(PickDateScreenEvent.OnDismissRequest) },
        screenFraction = DIALOG_SCREEN_FRACTION,
        title = {
            val titleText = remember(state.currentPickedDate) {
                state.currentPickedDate.toDayOfWeekMonthDayOfMonth()
            }
            BaseDialogBuilder.BaseDialogTitle(titleText = titleText)
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onEvent(PickDateScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickDateScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BaseDatePickerBuilder.MonthController(
                date = state.currentDate,
                onPrevClick = { onEvent(PickDateScreenEvent.OnPrevMonthClick) },
                onNextClick = { onEvent(PickDateScreenEvent.OnNextMonthClick) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            BaseDatePickerBuilder.MonthGrid(
                allDaysOfMonth = state.allDaysOfMonth,
                currentDate = state.currentDate,
                currentPickedDate = state.currentPickedDate,
                todayDate = state.todayDate,
                onDayOfMonthClick = { date ->
                    onEvent(PickDateScreenEvent.OnDateItemClick(date))
                }
            )
        }
    }
}