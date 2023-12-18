package com.example.bat.`object`

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

object RQ {
    private lateinit var RQ: RequestQueue

    fun initRQ (context: Context) {
        val requestQueue = Volley.newRequestQueue(context)

        RQ = requestQueue
    }

    fun getRQ(): RequestQueue {
        return RQ
    }
}