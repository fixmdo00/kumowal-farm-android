package com.example.bat.`object`

object SatuanProduk {
    fun getSatuan(produk : String) : String{
        var satuan = String()
        when (produk){
            "babi"-> {
                satuan = "Kilogram"
            }
            "ayam" -> {
                satuan = "Ekor"
            }
            "telur" -> {
                satuan = "Butir"
            }
        }
        return satuan
    }
}