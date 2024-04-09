package com.example.inhabitnow.android.ui.base

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.inhabitnow.android.R

object BaseCommonComponents {
    @Composable
    fun CreateTaskFAB(onClick: () -> Unit) {
        FloatingActionButton(onClick = onClick) {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
        }
    }
}