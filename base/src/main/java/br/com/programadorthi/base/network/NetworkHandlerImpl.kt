package br.com.programadorthi.base.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

class NetworkHandlerImpl(context: Context) : NetworkHandler {

    private val service = ContextCompat.getSystemService(context, ConnectivityManager::class.java)

    override fun hasInternetConnection(): Boolean =
        service?.activeNetworkInfo?.isConnectedOrConnecting ?: false

}