package com.taingdev.taipeitourapp.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

object Utils {
    @JvmStatic
    fun hasInternet(app: Application): Boolean {
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = cm.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }

    }


    @JvmStatic
    fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm =
                view.context.getSystemService<InputMethodManager>()
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}