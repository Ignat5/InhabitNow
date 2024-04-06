package com.example.inhabitnow.android.platform.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.inhabitnow.android.core.di.module.presentation.PresentationComponent
import kotlinx.coroutines.launch

class BootCompletedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            PresentationComponent.externalScope.launch {
                PresentationComponent.setUpAllRemindersUseCase()
                pendingResult.finish()
            }
        }
    }

}