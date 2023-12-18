package com.example.bat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bat.adapter.PilihAlamatAdapater
import com.example.bat.dataClass.Alamat
import com.example.bat.databinding.ActivityPilihAlamatBinding
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ
import org.json.JSONArray
import org.json.JSONObject

class PilihAlamatActivity : AppCompatActivity() {

    lateinit var binding : ActivityPilihAlamatBinding
    lateinit var recyclerView: RecyclerView
    lateinit var rvAdapter : PilihAlamatAdapater
    var listAlamat = ArrayList<Alamat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.RVAlamat
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        rvAdapter = PilihAlamatAdapater(listAlamat, this, binding.loadingProgressBar, this)
        recyclerView.adapter = rvAdapter

        ambilAlamatDariDb(Pengguna.id){
            binding.loadingProgressBar.visibility = View.GONE
            rvAdapter.list = listAlamat
            rvAdapter.notifyDataSetChanged()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            ambilAlamatDariDb(Pengguna.id){
                binding.swipeRefreshLayout.isRefreshing = false
                rvAdapter.list = listAlamat
                rvAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun ambilAlamatDariDb(user_id: String?, callback : (Boolean) -> Unit) {
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