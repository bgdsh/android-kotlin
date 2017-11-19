package net.goodbai.journaler.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import net.goodbai.journaler.execution.TaskExecutor


class MainService : Service(), DataSynchronization {

    private val tag = "Main service"
    private  var binder = getServiceBinder()
    private var executor = TaskExecutor.getInstance(1)

    override fun onCreate() {
        super.onCreate()
        Log.v(tag, "[ ON CREATE ]")
    }

    private fun getServiceBinder(): MainServiceBinder = MainServiceBinder()

    inner class MainServiceBinder: Binder() {
        fun getService(): MainService = this@MainService
    }

    override fun synchronize() {
        executor.execute {
            Log.i(tag, "Synchronizing data [ START ]")
            Thread.sleep(3000)
            Log.i(tag, "Synchronizing data [ END ]")
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.v(tag, "[ ON BIND ]")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(tag, "[ ON START COMMAND ]")
        synchronize()
        return Service.START_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val result = super.onUnbind(intent)
        Log.v(tag, "[ ON UNBIND ]")
        return result
    }

    override fun onDestroy() {
        synchronize()
        super.onDestroy()
        Log.v(tag, "[ ON DESTROY ]")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, "[ ON LOW MEMORY ]")
    }
}