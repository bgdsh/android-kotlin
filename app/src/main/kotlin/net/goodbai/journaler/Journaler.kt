package net.goodbai.journaler

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import net.goodbai.journaler.receiver.NetworkReceiver
import net.goodbai.journaler.service.MainService


class Journaler : Application() {
    companion object {
        var ctx: Context? = null
        val tag = "Journaler"
        val networkReceiver = NetworkReceiver()
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this.applicationContext
        Log.v(tag, "[ ON CREATE ]")
        // startService()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, "[ ON LOW MEMORY]")
        // stopService()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(tag, "[ ON TRIM MEMORY ]: $level")
    }

    private fun startService() {
        val serviceIntent = Intent(this, MainService::class.java)
        startService(serviceIntent)
    }

    private fun stopService() {
        val serviceIntent = Intent(this, MainService::class.java)
        stopService(serviceIntent)
    }
}