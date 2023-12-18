package com.example.bat.`object`

import android.util.Log
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException

object Pengguna {
    var id : String? = null
    var username : String? = null
    var nama : String? = null
    var alamat : String? = null
    var idAlamatPengiriman : String? = null
    var alamatPengiriman : String? = null
    var jenisAlamatPengiriman : String? = null
    var provinsiAlamatPengiriman : String? = null
    var kabupatenAlamatPengiriman : String? = null
    var kecamatanAlamatPengiriman : String? = null
    var kelurahanAlamatPengiriman : String? = null
    var jalanAlamatPengiriman : String? = null


    fun getDetailFromDb(user_id : String, callback : (Boolean) -> Unit){
        val url = "https://kumowal.my.id/api/user_get_detail.php?user_id=$user_id"
        Log.d("url2",url)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val respon = response.getJSONObject(0)
                    id = respon.getString("user_id")
                    username = respon.getString("username")
                    nama = respon.getString("name")
                    alamat = respon.getString("address")
                    idAlamatPengiriman = respon.getString("user_address_id")
                    alamatPengiriman = respon.getString("jalan") +", "+ respon.getString("kelurahan")+", "+ respon.getString("kecamatan") +", "+ respon.getString("kabupaten") +", "+ respon.getString("provinsi")
                    jenisAlamatPengiriman = respon.getString("jenis")
                    provinsiAlamatPengiriman = respon.getString("provinsi")
                    kabupatenAlamatPengiriman = respon.getString("kabupaten")
                    kecamatanAlamatPengiriman = respon.getString("kecamatan")
                    kelurahanAlamatPengiriman = respon.getString("kelurahan")
                    jalanAlamatPengiriman = respon.getString("jalan")
                    callback(true)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback(false)
                }
            },
            { error ->
                Log.d("err",error.toString())
                callback(false)
            })

        RQ.getRQ().add(jsonArrayRequest)
    }

    fun login (username : String, password : String, loading : ProgressBar, callback: (Boolean) -> Unit){

    }

    fun logout (){
        id = null
        username = null
        nama = null
        alamat = null
    }
}