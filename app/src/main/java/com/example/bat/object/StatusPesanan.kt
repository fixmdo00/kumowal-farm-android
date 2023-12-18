package com.example.bat.`object`

import android.view.View
import android.widget.LinearLayout

object StatusPesanan {

    fun getStatus(id : String) : String{
        var status = ""
        when (id){
            "1" -> {
                status = "Menunggu Konfirmasi"
            }
            "2" -> {
                status = "Persiapan Pengiriman"
            }
            "3" -> {
                status = "Dalam Pengiriman"
            }
            "4" -> {
                status = "Selesai"
            }
        }
        return status
    }

}