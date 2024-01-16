package com.example.bat.`object`

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.example.bat.dataClass.PesananData
import org.json.JSONArray
import org.json.JSONException

object Pesanan {

    var _pesanan = JSONArray()
    private val _livePesanan = MutableLiveData<JSONArray>()
    val livePesanan : LiveData<JSONArray> = _livePesanan

    fun getFromDB(progressBar: ProgressBar, callback : (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val user_id = Pengguna.id.toString()
        val url = "https://kumowal.my.id/api/pesanan_get.php?user_id=$user_id"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                progressBar.visibility = View.GONE
                try {
                    // Ambil data pertama dari JSON Array
                    _pesanan = response
                    _livePesanan.postValue(response)
                    callback(true)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback(false)
                }
            },
            { error ->
                Log.d("err",error.toString())
                callback(false)
            }
        )
        RQ.getRQ().add(jsonArrayRequest)
    }

    fun ubahStatus(status_baru : String, id_pesanan : String,progressBar: ProgressBar, callback: (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/pesanan_ubah_status.php"
        val postData = HashMap<String, String>()
        postData.put("status_baru",status_baru)
        postData.put("id_pesanan",id_pesanan)

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                progressBar.visibility = View.GONE
                val resp = response.trim()
                if (resp == "berhasil"){
                    callback(true)
                } else {
                    callback(false)
                }
            },
            Response.ErrorListener { error ->
                callback(false)
            }) {
            override fun getParams(): Map<String, String> {
                return postData
            }
        }
        RQ.getRQ().add(stringRequest)
    }

    fun getPesananArrayList() : ArrayList<PesananData> {
        var list = ArrayList<PesananData>()
        try {
            for( i in 0 until _pesanan.length()){
                val item = _pesanan.getJSONObject(i)
                list.add(
                    PesananData(
                        item.getString("order_id"),
                        item.getString("nama_pj"),
                        item.getString("jenis_pesanan"),
                        item.getString("jumlah_produk").toInt(),
                        item.getString("waktu_ditambahkan"),
                        item.getString("harga_satuan").toInt(),
                        item.getString("status"),
                        item.getString("name"),
                        item.getString("provinsi"),
                        item.getString("kabupaten"),
                        item.getString("kecamatan"),
                        item.getString("kelurahan"),
                        item.getString("jalan"),
                        item.getString("jenis"),
                        item.getString("harga_tawaran").toInt()
                    )
                )
            }
        }
        catch (e : Exception){
            Log.d("pzn pesanan catch", e.toString())
        }
        return list
    }

    fun getPesananArrayListByStatus(status : String) : ArrayList<PesananData> {
        var list = ArrayList<PesananData>()
        try {
            for( i in 0 until _pesanan.length()){
                val item = _pesanan.getJSONObject(i)
                if(item.getString("status") == status){
                    list.add(
                        PesananData(
                            item.getString("order_id"),
                            item.getString("nama_pj"),
                            item.getString("jenis_pesanan"),
                            item.getString("jumlah_produk").toInt(),
                            item.getString("waktu_ditambahkan"),
                            item.getString("harga_satuan").toInt(),
                            item.getString("status"),
                            item.getString("name"),
                            item.getString("provinsi"),
                            item.getString("kabupaten"),
                            item.getString("kecamatan"),
                            item.getString("kelurahan"),
                            item.getString("jalan"),
                            item.getString("jenis"),
                            item.getString("harga_tawaran").toInt()
                        )
                    )
                }
            }
        }
        catch (e : Exception){
            Log.d("pzn pesanan catch", e.toString())
        }
        return list
    }

    fun getPesananDetail(order_id : String) : PesananData {
        lateinit var list : PesananData
        try {
            for( i in 0 until _pesanan.length()){
                val item = _pesanan.getJSONObject(i)
                if (item.getString("order_id") == order_id){
                    list =
                        PesananData(
                            item.getString("order_id"),
                            item.getString("nama_pj"),
                            item.getString("jenis_pesanan"),
                            item.getString("jumlah_produk").toInt(),
                            item.getString("waktu_ditambahkan"),
                            item.getString("harga_satuan").toInt(),
                            item.getString("status"),
                            item.getString("name"),
                            item.getString("provinsi"),
                            item.getString("kabupaten"),
                            item.getString("kecamatan"),
                            item.getString("kelurahan"),
                            item.getString("jalan"),
                            item.getString("jenis"),
                            item.getString("harga_tawaran").toInt()
                        )
                }
            }
        }
        catch (e : Exception){
            Log.d("pzn pesanan catch", e.toString())
        }
        return list
    }
}