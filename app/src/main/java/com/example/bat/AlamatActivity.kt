package com.example.bat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bat.adapter.PilihAlamatAdapater
import com.example.bat.dataClass.Alamat
import com.example.bat.databinding.ActivityAlamatBinding
import com.example.bat.databinding.ActivityTambahAlamatBinding
import com.example.bat.`object`.DataAlamat
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ
import org.json.JSONArray

class AlamatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlamatBinding
    lateinit var recyclerView: RecyclerView
    lateinit var rvAdapter : PilihAlamatAdapater
    var listAlamat = ArrayList<Alamat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvProvinsi.text = Pengguna.provinsiAlamatPengiriman
        binding.tvKabupaten.text = Pengguna.kabupatenAlamatPengiriman
        binding.tvKecamatan.text = Pengguna.kecamatanAlamatPengiriman
        binding.tvKelurahan.text = Pengguna.kelurahanAlamatPengiriman
        binding.tvJalan.text = Pengguna.jalanAlamatPengiriman

        binding.btnTambah.setOnClickListener {
            intent = Intent(this, TambahAlamatActivity::class.java)
            startActivity(intent)
        }

    }

    private fun ambilDaftarAlamatDariDb(user_id: String?, callback : (Boolean) -> Unit) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/alamat_get_list.php?user_id=$user_id"
        Log.d("url",url)
        listAlamat.clear()
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url, JSONArray(),
            { response ->
                binding.loadingProgressBar.visibility = View.GONE
                Log.d("resp",response.toString())

                val status = response.getJSONObject(0)
                if (status.getString("user_address_id") == "gagal"){
                    Toast.makeText(this, "Gagal", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        for( i in 0 until response.length()){
                            val item = response.getJSONObject(i)
                            listAlamat.add(
                                Alamat(
                                    item.getString("user_address_id"),
                                    item.getString("jenis"),
                                    item.getString("provinsi"),
                                    item.getString("kabupaten"),
                                    item.getString("kecamatan"),
                                    item.getString("kelurahan"),
                                    item.getString("jalan")
                                )
                            )
                        }
                        callback(true)
                    }
                    catch (e : Exception){
                        Log.d("pzn catch", e.toString())
                        callback(false)
                    }
                }
            },
            { error ->
                callback(false)
                Log.d("pzn register err",error.toString())
                Toast.makeText(this, "Pendaftaran Berhasil", Toast.LENGTH_LONG).show()
            })
        RQ.getRQ().add(jsonArrayRequest)
    }
}