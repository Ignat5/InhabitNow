package com.example.inhabitnow.android.ui.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

private val DialogPadding = PaddingValues(all = 24.dp)

sealed interface BaseDialogTitle {
    data class Text(val titleText: String) : BaseDialogTitle
    data class Custom(val title: @Composable () -> Unit) : BaseDialogTitle
}

sealed interface BaseDialogActionButtons {

    data class Confirm(
        val confirmButton: @Composable () -> Unit
    ) : BaseDialogActionButtons

    data class ConfirmDismiss(
        val confirmButton: @Composable () -> Unit,
        val dismissButton: @Composable () -> Unit,
    ) : BaseDialogActionButtons
}

sealed interface BaseDialogBody {
    data class Message(val text: String) : BaseDialogBody
    sealed interface Custom : BaseDialogBody {
        val bodyContent: @Composable () -> Unit

        data class Static(override val bodyContent: @Composable () -> Unit) : Custom
        data class Scrollable(override val bodyContent: @Composable () -> Unit) : Custom
    }
}

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    baseDialogBody: BaseDialogBody,
    modifier: Modifier = Modifier,
    baseDialogTitle: BaseDialogTitle? = null,
    baseDialogActionButtons: BaseDialogActionButtons? = null,
    properties: DialogProperties = DialogProperties(),
) {
    BaseDialog(
        onDismissRequest,
        modifier.then(
            run {
                if (baseDialogBody is BaseDialogBody.Custom.Scrollable) {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                    val isPortrait = screenHeight >= screenWidth
                    if (isPortrait) {
                        Modifier.height(screenHeight / 2)
                    } else {
                        Modifier
                            .height(screenHeight)
                            .padding(vertical = 16.dp)
                    }
                } else Modifier
            }
        ),
        properties
    ) {
        baseDialogTitle?.let {
            when (baseDialogTitle) {
                is BaseDialogTitle.Text -> {
                    BaseDialogTitle(baseDialogTitle.titleText)
                }

                is BaseDialogTitle.Custom -> {
                    baseDialogTitle.title()
                }
            }
            AfterTitleSpacer()
        }
        baseDialogBody.let {
            when (baseDialogBody) {
                is BaseDialogBody.Message -> {}
                is BaseDialogBody.Custom -> {
                    when (baseDialogBody) {
                        is BaseDialogBody.Custom.Static -> {
                            baseDialogBody.bodyContent()
                        }

                        is BaseDialogBody.Custom.Scrollable -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                HorizontalDivider()
                                baseDialogBody.bodyContent()
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
        baseDialogActionButtons?.let {
            BeforeButtonsSpacer()
            when (baseDialogActionButtons) {
                is BaseDialogActionButtons.Confirm -> {
                    RowActionButtons(confirmButton = {
                        baseDialogActionButtons.confirmButton()
                    })
                }

                is BaseDialogActionButtons.ConfirmDismiss -> {
                    RowActionButtons(
                        confirmButton = {
                            baseDialogActionButtons.confirmButton()
                        },
                        dismissButton = {
                            baseDialogActionButtons.dismissButton()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(DialogPadding)) {
                content()
            }
        }
    }
}

@Composable
private fun BaseDialogTitle(titleText: String) {
    Text(
        text = titleText,
        color = AlertDialogDefaults.titleContentColor,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}

@Composable
private fun RowActionButtons(
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        dismissButton?.invoke()
        confirmButton.invoke()
    }
}

@Composable
private fun AfterTitleSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun BeforeButtonsSpacer() {
    Spacer(modifier = Modifier.height(24.dp))
}