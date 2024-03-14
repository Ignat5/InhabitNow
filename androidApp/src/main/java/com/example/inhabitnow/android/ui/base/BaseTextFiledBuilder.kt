package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object BaseTextFiledBuilder {

    @Composable
    fun BaseOutlinedTextField(
        text: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        isInitFocused: Boolean = false,
        initFocusRequester: @Composable () -> FocusRequester = {
            remember { FocusRequester() }
        },
        label: (@Composable () -> Unit)? = null,
        supportingText: (@Composable () -> Unit)? = null,
        singleLine: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
    ) {
        val focusRequester = initFocusRequester()
        val textFiled = remember(text) {
            TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            )
        }
        OutlinedTextField(
            value = textFiled,
            onValueChange = {
                onValueChange(it.text)
            },
            label = label,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            supportingText = supportingText,
            singleLine = singleLine,
            modifier = modifier
                .focusRequester(focusRequester)
        )
        if (isInitFocused) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}