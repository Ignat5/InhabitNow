package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val DEFAULT_DEBOUNCE_MILLIS = 200L

object BaseTextFiledBuilder {

    @OptIn(FlowPreview::class)
    @Composable
    fun BaseOutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        valueValidator: (String) -> Boolean = { true },
        label: (@Composable () -> Unit)? = null,
        supportingText: (@Composable () -> Unit)? = null,
        singleLine: Boolean = false,
        minLines: Int = 1,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
    ) {
        var currentValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                )
            )
        }
        OutlinedTextField(
            value = currentValue,
            onValueChange = { textFieldValue ->
                if (valueValidator(textFieldValue.text)) {
                    currentValue = textFieldValue
                }
            },
            label = label,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            supportingText = supportingText,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            modifier = modifier
        )

        LaunchedEffect(Unit) {
            snapshotFlow { currentValue }
                .debounce(DEFAULT_DEBOUNCE_MILLIS)
                .collectLatest { value ->
                    onValueChange(value.text)
                }
        }
    }
}