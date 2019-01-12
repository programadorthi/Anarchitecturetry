package br.com.programadorthi.base.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

class NetworkHandlerImpl(private val context: Context) : NetworkHandler {

    override fun hasInternetConnection(): Boolean {
        val service =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java) ?: return false
        return service.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

}