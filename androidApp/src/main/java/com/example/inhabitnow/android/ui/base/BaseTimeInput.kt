package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun BaseTimeInput(
    hours: Int,
    minutes: Int,
    onInputUpdateHours: (Int) -> Unit,
    onInputUpdateMinutes: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val initHours = remember { hours }
    val initMinutes = remember { minutes }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeInputField(
                inputType = TimeInputType.Hours,
                initIndex = initHours,
                onCurrentIndexChange = onInputUpdateHours,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = ":",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TimeInputField(
                inputType = TimeInputType.Minutes,
                initIndex = initMinutes,
                onCurrentIndexChange = onInputUpdateMinutes,
                modifier = Modifier.weight(1f)
            )
        }
//        Box(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .fillMaxWidth()
//                .height(TextFieldDefaults.MinHeight)
//                .border(
//                    width = TextFieldDefaults.UnfocusedIndicatorThickness,
//                    color = MaterialTheme.colorScheme.outline,
//                    shape = RoundedCornerShape(25)
//                )
//        )
    }
}

private enum class TimeInputType(val range: IntRange) {
    Hours(IntRange(0, 23)),
    Minutes(IntRange(0, 59))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimeInputField(
    inputType: TimeInputType,
    initIndex: Int,
    onCurrentIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val targetHeight = remember {
        TextFieldDefaults.MinHeight
    }
    val lazyListState = rememberLazyListState(initIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val rangeList = remember(inputType) { inputType.range.toList() }

    Box(
        modifier = modifier
            .height(targetHeight)
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            ),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = lazyListState,
            flingBehavior = flingBehavior,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            items(rangeList) { item ->
                val itemValue = remember {
                    if (item <= 9) "0$item" else "$item"
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(targetHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (!lazyListState.isScrollInProgress) {
            onCurrentIndexChange(lazyListState.firstVisibleItemIndex)
        }
    }
}

//@Composable
//fun BaseTimeInput(
//    inputHours: String,
//    inputMinutes: String,
//    onInputValueHoursUpdate: (String) -> Unit,
//    onInputValueMinutesUpdate: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val focusManager = LocalFocusManager.current
//    Row(
//        modifier = modifier,
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        OutlinedTextField(
//            value = inputHours,
//            onValueChange = onInputValueHoursUpdate,
//            label = {
//                Text(text = "hours")
//            },
//            keyboardActions = KeyboardActions {
//                focusManager.moveFocus(FocusDirection.Next)
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Decimal,
//                imeAction = ImeAction.Next
//            ),
//            singleLine = true,
//            modifier = Modifier
//                .weight(1f)
//        )
//        Text(
//            text = ":",
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//
//        val minutesValue = remember(inputMinutes) {
//            TextFieldValue(
//                text = inputMinutes,
//                selection = TextRange(inputMinutes.length)
//            )
//        }
//
//        OutlinedTextField(
//            value = minutesValue,
//            onValueChange = {
//                onInputValueMinutesUpdate(it.text)
//            },
//            label = {
//                Text(text = "minutes")
//            },
//            keyboardActions = KeyboardActions {
//                focusManager.clearFocus()
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Decimal,
//                imeAction = ImeAction.Done
//            ),
//            singleLine = true,
//            modifier = Modifier.weight(1f)
//        )
//    }
//}