package com.mobicom.s16.csarchers.notification_senders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobicom.s16.csarchers.notification_senders.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.showNotification(
            context,
            "Weekly Score Reset",
            "Sharpen those arrows ARCHers, take back the crown on the leaderboards!"
        )
    }
}
