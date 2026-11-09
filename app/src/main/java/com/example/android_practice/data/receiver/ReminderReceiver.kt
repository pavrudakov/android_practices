package com.example.android_practice.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.android_practice.data.notification.ReminderNotifier

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val personName = intent.getStringExtra(EXTRA_PERSON_NAME) ?: ""
        ReminderNotifier.showNotification(context, personName)
    }

    companion object {
        const val EXTRA_PERSON_NAME = "extra_person_name"
    }
}
