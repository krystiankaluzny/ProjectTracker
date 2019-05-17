package org.projecttracker.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.support.annotation.RequiresApi


class NetworkStateMonitor constructor(private val context: Context) {

    var isNetworkConnected = false
        private set

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    inline fun ifConnected(block: () -> Unit) {
        if (isNetworkConnected) {
            block()
        }
    }

    inline fun ifDisconnected(block: () -> Unit) {
        if (!isNetworkConnected) {
            block()
        }
    }

    fun startMonitoring() {

        checkConnection()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            registerNetworkCallback()
        } else {
            registerReceiver()
        }
    }

    fun stopMonitoring() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            unregisterNetworkCallback()
        } else {
            unregisterReceiver()
        }
    }

    private fun checkConnection() {
        isNetworkConnected = connectivityManager.activeNetworkInfo?.isConnected == true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun registerNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    private fun registerReceiver() {
        context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun unregisterReceiver() {
        context.unregisterReceiver(networkReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            isNetworkConnected = true
        }

        override fun onLost(network: Network?) {
            isNetworkConnected = false
        }
    }

    private val networkReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            checkConnection()
        }
    }
}