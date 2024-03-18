package com.example.inhabitnow.android.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import com.example.inhabitnow.android.R
import kotlin.enums.EnumEntries

object BaseInputBuilder {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun <T : Enum<T>> BaseOutlinedInputDropdown(
        allOptions: EnumEntries<T>,
        currentOption: T,
        optionText: (T) -> String,
        onOptionClick: (T) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var isDropDownExpanded by remember {
            mutableStateOf(false)
        }
        val focusManager = LocalFocusManager.current
        ExposedDropdownMenuBox(
            expanded = isDropDownExpanded,
            onExpandedChange = {
                isDropDownExpanded = it
            },
            modifier = modifier
        ) {
            OutlinedTextField(
                value = optionText(currentOption),
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                readOnly = true,
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false }
            ) {
                allOptions.forEach { option ->
                    val text = remember {
                        optionText(option)
                    }
                    DropdownMenuItem(
                        text = {
                            Text(text = text)
                        },
                        onClick = {
                            onOptionClick(option)
                            isDropDownExpanded = false
                        }
                    )
                }
            }
        }

        LaunchedEffect(isDropDownExpanded) {
            if (!isDropDownExpanded) {
                focusManager.clearFocus(true)
            }
        }
    }

    @Composable
    fun BaseOutlinedInputBox(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .clickable { onClick() }
                .heightIn(min = TextFieldDefaults.MinHeight)
                .border(
                    width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                    color = MaterialTheme.colorScheme.outline,
                    shape = OutlinedTextFieldDefaults.shape
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }

}