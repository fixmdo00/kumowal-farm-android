package com.example.bat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.bat.databinding.ActivityBuatPesananAyamBinding
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ
import org.json.JSONException
import java.text.NumberFormat
import java.util.Locale

class BuatPesananAyamActivity : AppCompatActivity() {
    lateinit var binding : ActivityBuatPesananAyamBinding
    var hargaAyam = 0
    var totalHarga = 0
    lateinit var jenisBeratAyam : String
    lateinit var berat : String
    lateinit var namaPj : String
    lateinit var idAlamat : String
    lateinit var alamat : String
    lateinit var jenisAlamat : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatPesananAyamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ambilHargaAyam(){
            when (it){
                true -> {
                    binding.tvHarga.text = "Harga per ekor : Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(hargaAyam)
                }
                false ->{}
            }
        }

        binding.itEkor.setOnEditorActionListener { _, actionId, _ ->
            binding.itEkor.clearFocus()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.itEkor.text.toString().isNullOrBlank()){
                    binding.tvTotalHarga.text = "Total harga : Rp 0"
                } else {
                    totalHarga = hargaAyam * binding.itEkor.text.toString().toInt()
                    binding.tvTotalHarga.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                    return@setOnEditorActionListener false
                }
            }
            false
        }

        binding.itEkor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Memastikan hanya ketika terjadi perubahan teks dan teks bukan kosong
                if (!s.isNullOrBlank()) {
                    totalHarga = hargaAyam * binding.itEkor.text.toString().toInt()
                    binding.tvTotalHarga.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                } else {
                    binding.tvTotalHarga.text = "Total harga : Rp 0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Method kosong yang diperlukan oleh interface
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Method kosong yang diperlukan oleh interface
            }
        })

        if(!intent.getStringExtra("idAlamat").isNullOrBlank()){
            ambilDataAlamat(intent.getStringExtra("idAlamat")){
                when (it){
                    true ->{
                        binding.tvJenisAlamat.text = jenisAlamat
                        binding.tvAlamat.text = alamat
                    }
                    false -> {
                        Toast.makeText(this, "Gaggal Memuat Alamat", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else if (!Pengguna.idAlamatPengiriman.isNullOrBlank()){
            idAlamat = Pengguna.idAlamatPengiriman.toString()
            alamat = Pengguna.alamatPengiriman.toString()
            jenisAlamat = Pengguna.jenisAlamatPengiriman.toString()

            binding.tvJenisAlamat.text = jenisAlamat
            binding.tvAlamat.text = alamat
        } else {
            Toast.makeText(this, "Gaggal Memuat Alamat", Toast.LENGTH_LONG).show()
        }

//        SELECTOR BERAT AYAM
//        val daftarAlamat = resources.getStringArray(R.array.berat_ayam)
//        val arrayAdapater = ArrayAdapter(this, R.layout.item_dropdown_berat_ayam, daftarAlamat)
//        binding.itBeratAyam.setAdapter(arrayAdapater)
//        binding.itBeratAyam.setOnItemClickListener { adapterView, view, i, l ->
//            Log.d("pzn dropdown",adapterView.getItemAtPosition(i).toString())
//            bagianPotongan = adapterView.getItemAtPosition(i).toString()
//        }
//
//        if (daftarAlamat.isNotEmpty()) {
//            bagianPotongan = daftarAlamat[0] // Mengambil nilai pertama dari array
//        }
//        END SELECTOR BERAT AYAM

        binding.btnBeli.setOnClickListener {
            if (binding.itNama.text.isNullOrBlank() && binding.itEkor.text.isNullOrBlank()){
                Toast.makeText(this, "Masukkan data yang lengkap", Toast.LENGTH_LONG).show()
            } else {
                buatPesanan(binding.itEkor.text.toString(), binding.itNama.text.toString(), idAlamat)
            }
        }

        binding.btnUbahAlamat.setOnClickListener {
            intent = Intent(this, PilihAlamatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun ambilHargaAyam(callback: (Boolean) -> Unit) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/harga_get.php?item=ayam"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                binding.loadingProgressBar.visibility = View.GONE
                val resp = response.trim()
                if (resp == "gagal"){
                    Toast.makeText(this, "Gagal mendapatkan harga", Toast.LENGTH_LONG).show()
                    callback(false)

                } else {
                    hargaAyam = resp.toInt()
                    callback(true)
                }
            },
            Response.ErrorListener { error ->
                binding.loadingProgressBar.visibility = View.GONE
                Toast.makeText(this, "eroor", Toast.LENGTH_LONG).show()
                callback(false)
            }){
        }
        RQ.getRQ().add(stringRequest)
    }

    private fun buatPesanan(ekor: String, namaPj: String, idAlamat: String) {
        val url = "https://kumowal.my.id/api/pesanan_buat.php"
        binding.loadingProgressBar.visibility = View.VISIBLE
        val postData = HashMap<String, String>()
        postData.put("jenis_pesanan","ayam")
        postData.put("ekor",ekor)
        postData.put("nama_pj", namaPj)
        postData.put("id_alamat", idAlamat)
        postData.put("userId",Pengguna.id.toString())
        postData.put("harga",hargaAyam.toString())

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                binding.loadingProgressBar.visibility = View.GONE
                val resp = response.trim()
                if (resp == "berhasil"){
                    Toast.makeText(this, "Pesanan Berhasil Dibuat", Toast.LENGTH_LONG).show()
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
                    Log.d("pzn",resp)
                }
            },
            Response.ErrorListener { error ->
                Log.d("pzn add keranjang item err",error.toString())
                Toast.makeText(this, "eroor", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                return postData
            }
        }
        RQ.getRQ().add(stringRequest)
    }

    private fun ambilDataAlamat(id_alamat: String?, callback : (Boolean) -> Unit) {
        val url = "https://kumowal.my.id/api/alamat_get_by_id.php?alamat_id=$id_alamat"
        Log.d("urls",url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    idAlamat = response.getString("user_address_id")
                    jenisAlamat = response.getString("jenis")
                    alamat = response.getString("jalan") + ", " + response.getString("kelurahan") + ", " + response.getString("kecamatan") + ", " + response.getString("kabupaten") + ", " + response.getString("provinsi")
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

        RQ.getRQ().add(jsonObjectRequest)
    }
}