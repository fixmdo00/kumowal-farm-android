package com.example.bat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.bat.databinding.ActivityTambahAlamatBinding
import com.example.bat.`object`.DataAlamat
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ

class TambahAlamatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTambahAlamatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.itProvinsi.setText(Pengguna.provinsiAlamatPengiriman)
        binding.itKabupaten.setText(Pengguna.kabupatenAlamatPengiriman)
        binding.itKecamatan.setText(Pengguna.kecamatanAlamatPengiriman)
        binding.itKelurahan.setText(Pengguna.kelurahanAlamatPengiriman)
        binding.itJalan.setText(Pengguna.jalanAlamatPengiriman)

        binding.btnSimpan.setOnClickListener {
            if(
                binding.itProvinsi.text.toString().isBlank() ||
                binding.itKabupaten.text.toString().isBlank() ||
                binding.itKecamatan.text.toString().isBlank() ||
                binding.itKelurahan.text.toString().isBlank() ||
                binding.itJalan.text.toString().isBlank()
            ) {
                Toast.makeText(this,"Data tidak lengkap", Toast.LENGTH_LONG).show()
            } else {
                ubahAlamat(
                    binding.itProvinsi.text.toString(),
                    binding.itKabupaten.text.toString(),
                    binding.itKecamatan.text.toString(),
                    binding.itKelurahan.text.toString(),
                    binding.itJalan.text.toString()
                )
            }
        }

        binding.btnKembali.setOnClickListener {
            onBackPressed()
        }
    }

    private fun ubahAlamat(provinsi: String, kabupaten: String, kecamatan: String, kelurahan: String, jalan: String) {
        val url = "https://kumowal.my.id/api/alamat_ubah.php"
        binding.loadingProgressBar.visibility = View.VISIBLE
        val postData = HashMap<String, String>()

        postData.put("user_id",Pengguna.id.toString())
        postData.put("provinsi",provinsi)
        postData.put("kabupaten", kabupaten)
        postData.put("kecamatan", kecamatan)
        postData.put("kelurahan",kelurahan)
        postData.put("jalan",jalan)

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                val resp = response.trim()
                if (resp == "berhasil"){
                    Toast.makeText(this, "Alamat berhasil di ubah", Toast.LENGTH_LONG).show()
                    Pengguna.getDetailFromDb(Pengguna.id.toString()){
                        binding.loadingProgressBar.visibility = View.GONE
                        when(it){
                            true -> {
                                intent = Intent(this, AlamatActivity::class.java)
                                startActivity(intent)
                            }
                            false -> {
                                Toast.makeText(this, "Gagal update data client", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                } else {
                    Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "eroor", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return postData
            }
        }
        RQ.getRQ().add(stringRequest)
    }

}