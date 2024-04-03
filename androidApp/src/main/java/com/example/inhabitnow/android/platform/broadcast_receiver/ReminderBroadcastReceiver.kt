package com.example.inhabitnow.android.platform.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.inhabitnow.android.platform.util.ReminderUtil

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        intent.getStringExtra(ReminderUtil.REMINDER_ID_KEY)?.let { reminderId ->

        }
    }

}