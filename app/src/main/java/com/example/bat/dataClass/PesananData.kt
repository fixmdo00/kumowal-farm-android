package com.example.bat.dataClass

data class PesananData(
    var id_pesanan : String,
    var nama_pj : String,
    var jenis_produk : String,
    var jumlah_produk : Int,
    var waktu : String,
    var harga_satuan : Int,
    var status : String,
    var nama_user : String,
    var pengiriman_provinsi : String,
    var pengiriman_kabupaten : String,
    var pengiriman_kecamatan : String,
    var pengiriman_kelurahan : String,
    var pengiriman_jalan : String,
    var pengiriman_jenis : String
)
