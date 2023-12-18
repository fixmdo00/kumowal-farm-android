package com.example.bat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.bat.dataClass.PesananData
import com.example.bat.databinding.ActivityDetailPesananBinding
import com.example.bat.`object`.Pesanan
import com.example.bat.`object`.SatuanProduk
import com.example.bat.`object`.StatusPesanan
import java.text.NumberFormat
import java.util.Locale

class DetailPesananActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailPesananBinding
    private lateinit var id_pesanan : String
    private lateinit var detail_pesanan : PesananData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_pesanan = intent.getStringExtra("id_pesanan").toString()
        detail_pesanan = Pesanan.getPesananDetail(id_pesanan)

        val alamat_pengiriman = detail_pesanan.pengiriman_jalan + ", " + detail_pesanan.pengiriman_kelurahan + ", " + detail_pesanan.pengiriman_kecamatan + ", " + detail_pesanan.pengiriman_kabupaten + ", " + detail_pesanan.pengiriman_provinsi
        val banyaknya_produk = detail_pesanan.jumlah_produk.toString() + " " + SatuanProduk.getSatuan(detail_pesanan.jenis_produk)
        val total_harga = "Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(detail_pesanan.harga_satuan * detail_pesanan.jumlah_produk)
        binding.tvStatus.text = StatusPesanan.getStatus(detail_pesanan.status)
        binding.tvAlamatPengiriman.text = alamat_pengiriman
        binding.tvJenisAlamatPengiriman.text = detail_pesanan.pengiriman_jenis
        binding.tvPjPesanan.text = detail_pesanan.nama_pj
        binding.tvJenisProduk.text = detail_pesanan.jenis_produk.capitalize()
        binding.tvBanyaknyaProduk.text = banyaknya_produk
        binding.tvHargaSatuan.text = "Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(detail_pesanan.harga_satuan)
        binding.tvTotalHarga.text = total_harga.toString()

        binding.btnKembali.setOnClickListener {
            onBackPressed()
        }

        when(detail_pesanan.status){
            "1" -> {
                binding.containerBtn.visibility = View.GONE
            }
            "2" -> {
                binding.containerBtn.visibility = View.GONE
            }
            "3" -> {
                binding.tvPesan.text = "Apakah pesanan telah di terima ?"
                binding.btnKonfirmasi.hint = "Pesanan telah di terima"
                binding.btnKonfirmasi.setOnClickListener {
                    Pesanan.ubahStatus("4",detail_pesanan.id_pesanan, binding.loadingProgressBar){
                        when (it){
                            true -> {
                                Toast.makeText(this,"Berhasil", Toast.LENGTH_LONG).show()
                                Pesanan.getFromDB(binding.loadingProgressBar){}
                                onBackPressed()
                            }
                            false -> {
                                Toast.makeText(this,"Gagal", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            "4" -> {
                binding.containerBtn.visibility = View.GONE
            }
        }
    }
}