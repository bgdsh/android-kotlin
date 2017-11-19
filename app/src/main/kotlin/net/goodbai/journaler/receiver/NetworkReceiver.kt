package net.goodbai.journaler.receiver

import android.content.*
import android.net.ConnectivityManager
import android.os.IBinder
import android.util.Log
import net.goodbai.journaler.service.MainService


class NetworkReceiver : BroadcastReceiver() {
    private var isBound = false
    private val tag = "Network receiver"
    private var service: MainService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            if (binder is MainService.MainServiceBinder) {
                service = binder.getService()
                service?.synchronize()
            }
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            Log.v(tag, "Connectivity [ AVAILABLE ]")
            if (service == null) {
                val intent = Intent(context, MainService::class.java)
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
                isBound = true
            } else {
                service?.synchronize()
            }
        } else {
            Log.w(tag, "Connectivity [ UNAVAILABLE ]")
            if (isBound) {
                context?.unbindService(serviceConnection)
                isBound = false
            }
        }
    }

}