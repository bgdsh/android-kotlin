package net.goodbai.journaler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ShutdownReceiver:BroadcastReceiver() {
    val tag = "Shutdown receiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(tag, "Shutting down.")
    }
}
