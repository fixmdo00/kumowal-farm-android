package com.example.bat.`object`

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONException

object DataAlamat {
    var jsonArrayProvinsi = JSONArray()
    var jsonArrayKabupaten = JSONArray()
    var jsonArrayKecamatan = JSONArray()
    var jsonArrayKelurahan = JSONArray()

    fun getProvinsiFromDB(progressBar: ProgressBar, callback : (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/daftar_alamat_get.php?tabel=provinces"
        jsonArrayReq(url){
            progressBar.visibility = View.GONE
            if (it != null) {
                callback(true)
                jsonArrayProvinsi = it
            } else {callback(false)}
        }

    }

    fun getKabupatenFromDB(id_provinsi: String, progressBar: ProgressBar, callback : (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/daftar_alamat_get.php?tabel=provinces&id_name=province_id&id_param=$id_provinsi"
        jsonArrayReq(url){
            progressBar.visibility = View.GONE
            if (it != null) {
                jsonArrayKabupaten = it
            }
        }
    }

    fun getKecamatanFromDB(id_kabupaten: String, progressBar: ProgressBar, callback : (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/daftar_alamat_get.php?tabel=provinces&id_name=regency_id&id_param=$id_kabupaten"
        jsonArrayReq(url){
            progressBar.visibility = View.GONE
            if (it != null) {
                jsonArrayKecamatan = it
            }
        }
    }

    fun getKelurahanFromDB(id_kecamatan: String, progressBar: ProgressBar, callback : (Boolean) -> Unit){
        progressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/daftar_alamat_get.php?tabel=provinces&id_name=district_id &id_param=$id_kecamatan"
        jsonArrayReq(url){
            progressBar.visibility = View.GONE
            if (it != null) {
                jsonArrayKelurahan = it
            }
        }
    }

    fun getMutableList(jsonArray : JSONArray): MutableList<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val name = item.getString("name")
            list.add(name)
        }
        return list
    }




    fun jsonArrayReq(url: String, callback: (JSONArray?) -> Unit) {
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Handle the JSON response
                    callback(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback(null) // Signal the failure in parsing JSON
                }
            },
            { error ->
                Log.d("err", error.toString())
                callback(null) // Signal the request failure
            }
        )
        // Assuming RQ.getRQ() returns the RequestQueue instance
        RQ.getRQ().add(jsonArrayRequest)
    }


}