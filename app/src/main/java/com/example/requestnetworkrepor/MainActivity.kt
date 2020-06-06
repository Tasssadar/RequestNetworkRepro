package com.example.requestnetworkrepor

import android.content.Context
import android.net.*
import android.net.wifi.WifiNetworkSpecifier
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var mConnMgr: ConnectivityManager? = null
    private var mListener: NetworkListener = NetworkListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mConnMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        findViewById<Button>(R.id.button).setOnClickListener {
            val ssid = findViewById<EditText>(R.id.ssid).text.toString()
            val pass = findViewById<EditText>(R.id.password).text.toString()

            val spec = WifiNetworkSpecifier.Builder().apply {
                setSsid(ssid)
                if(pass.isNotEmpty())
                    setWpa2Passphrase(pass)
            }.build()

            val req = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(spec)
                    .build()

            try {
                mConnMgr!!.requestNetwork(req, mListener)
            } catch(e: RuntimeException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mConnMgr?.unregisterNetworkCallback(mListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private class NetworkListener : ConnectivityManager.NetworkCallback() {
        private val  TAG = "NetworkListener"
        override fun onAvailable( network: Network?) {
            Log.i(TAG, "onAvailable $network")
        }

        override fun onLosing(network: Network?, maxMsToLive: Int) {
            Log.i(TAG, "onLosing $network $maxMsToLive")
        }

        override fun onLost(network: Network?) {
            Log.i(TAG, "onLost $network")
        }

        override fun onUnavailable() {
            Log.i(TAG, "onUnavailable")
        }

        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
            Log.i(TAG, "onCapabilitiesChanged $network $networkCapabilities")
        }

        override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
            Log.i(TAG, "onLinkPropertiesChanged $network $linkProperties")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            Log.i(TAG, "onBlockedStatusChanged $network $blocked")
        }
    }
}
