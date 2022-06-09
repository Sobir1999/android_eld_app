package com.iosix.eldblesample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.iosix.eldblesample.MyApplication

class NetworkHelper {

    companion object{
        fun hasInternetConnection(): Boolean {
            var result = false
            val connectivityManager =
                MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result
        }


        @Throws(Exception::class)
        fun setMobileDataEnabled() {
            val conman =
                MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var conmanClass: Class<*>? = null
            try {
                conmanClass = Class.forName(conman.javaClass.name)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            assert(conmanClass != null)
            val iConnectivityManagerField = conmanClass!!.getDeclaredField("mService")
            iConnectivityManagerField.isAccessible = true
            val iConnectivityManager = iConnectivityManagerField[conman]
            val iConnectivityManagerClass = Class.forName(iConnectivityManager.javaClass.name)
            val setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod(
                "setMobileDataEnabled",
                java.lang.Boolean.TYPE
            )
            setMobileDataEnabledMethod.isAccessible = true
            if (!hasInternetConnection()) setMobileDataEnabledMethod.invoke(
                iConnectivityManager,
                true
            )
        }
    }
}