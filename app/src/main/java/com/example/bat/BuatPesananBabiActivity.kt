package com.example.bat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.bat.databinding.ActivityBuatPesananBabiBinding
import com.example.bat.`object`.Pengguna
import com.example.bat.`object`.RQ
import org.json.JSONException
import java.text.NumberFormat
import java.util.Locale

class BuatPesananBabiActivity : AppCompatActivity() {
    lateinit var binding : ActivityBuatPesananBabiBinding
    var hargaBabi = 0
    var totalHarga = 0
    var diskon5 = 0
    var diskon10 = 0
    val diskonList = ArrayList<String>()
    var selectedDiskon = 0
    lateinit var bagianPotongan : String
    lateinit var berat : String
    lateinit var namaPj : String
    lateinit var idAlamat : String
    lateinit var alamat : String
    lateinit var jenisAlamat : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatPesananBabiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerDiskon = binding.spinnerDiskon
        val adapterDiskon = ArrayAdapter(this, android.R.layout.simple_spinner_item, diskonList)
        adapterDiskon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiskon.adapter =adapterDiskon

        diskonList.add("Belum dipilih")
        adapterDiskon.notifyDataSetChanged()

        ambilHargaBabi(){
            when (it){
                true -> {
                    binding.tvHarga.text = "Harga per kilogram : Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(hargaBabi)
                }
                false ->{}
            }
        }

        binding.itBerat.setOnEditorActionListener { _, actionId, _ ->
            binding.itBerat.clearFocus()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.itBerat.text.toString().isNullOrBlank()){
                    binding.tvTotalHarga.text = "Total harga : Rp 0"
                } else {
                    totalHarga = hargaBabi * binding.itBerat.text.toString().toInt()
                    diskon5 = (totalHarga * 0.95).toInt()
                    diskon10 = (totalHarga * 0.90).toInt()
                    diskonList.clear()
                    diskonList.add("Belum dipilih")
                    diskonList.add("Diskon 5%, jadi Rp. " + NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(diskon5))
                    diskonList.add("Diskon 10%, jadi Rp. " + NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(diskon10))
                    adapterDiskon.notifyDataSetChanged()
                    binding.tvTotalHarga.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                    binding.tvSubtotaltotal.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                    return@setOnEditorActionListener false
                }
            }
            false
        }

        binding.itBerat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Memastikan hanya ketika terjadi perubahan teks dan teks bukan kosong
                if (!s.isNullOrBlank()) {
                    totalHarga = hargaBabi * binding.itBerat.text.toString().toInt()
                    diskon5 = (totalHarga * 0.95).toInt()
                    diskon10 = (totalHarga * 0.90).toInt()
                    diskonList.clear()
                    diskonList.add("Belum dipilih")
                    diskonList.add("Diskon 5%, jadi Rp. " + NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(diskon5))
                    diskonList.add("Diskon 10%, jadi Rp. " + NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(diskon10))
                    adapterDiskon.notifyDataSetChanged()
                    binding.tvTotalHarga.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                    binding.tvSubtotaltotal.text =
                        "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                            .format(totalHarga)
                } else {
                    binding.tvTotalHarga.text = "Total harga : Rp 0"
                    binding.tvSubtotaltotal.text = "Total harga : Rp 0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Method kosong yang diperlukan oleh interface
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Method kosong yang diperlukan oleh interface
            }
        })

        spinnerDiskon.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("spinner parent",parent.toString())
                Log.d("spinner view",view.toString())
                Log.d("spinner pos",position.toString())
                Log.d("spinner id",id.toString())
                when (position){
                    1 -> {
                        selectedDiskon = (totalHarga * 0.95).toInt()
                        binding.tvSubtotaltotal.text =
                            "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                                .format(selectedDiskon)
                    }
                    2 -> {
                        selectedDiskon = (totalHarga * 0.90).toInt()
                        binding.tvSubtotaltotal.text =
                            "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                                .format(selectedDiskon)
                    }
                    0 -> {
                        selectedDiskon = 0
                        binding.tvSubtotaltotal.text =
                            "Total harga : Rp " + NumberFormat.getNumberInstance(Locale.getDefault())
                                .format(totalHarga)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

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

        val daftarJenis = resources.getStringArray(R.array.jenis_potongan)
        val arrayAdapater = ArrayAdapter(this, R.layout.item_dropdown_jenis_potongan, daftarJenis)
        binding.itJenisPotongan.setAdapter(arrayAdapater)
        binding.itJenisPotongan.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("pzn dropdown",adapterView.getItemAtPosition(i).toString())
            bagianPotongan = adapterView.getItemAtPosition(i).toString()
        }

        if (daftarJenis.isNotEmpty()) {
            bagianPotongan = daftarJenis[0] // Mengambil nilai pertama dari array
        }


        binding.btnBeli.setOnClickListener {
            if (binding.itNama.text.isNullOrBlank() || binding.itBerat.text.isNullOrBlank()){
                Toast.makeText(this, "Masukkan data yang lengkap", Toast.LENGTH_LONG).show()
            } else {
                buatPesanan(
                    bagianPotongan,
                    binding.itBerat.text.toString(),
                    binding.itNama.text.toString(),
                    idAlamat,
                    selectedDiskon.toString())
            }
        }

        binding.btnUbahAlamat.setOnClickListener {
            intent = Intent(this, PilihAlamatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun ambilHargaBabi(callback: (Boolean) -> Unit) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        val url = "https://kumowal.my.id/api/harga_get.php?item=babi"
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
                    hargaBabi = resp.toInt()
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

    private fun buatPesanan(bagianPotongan: String, berat: String, namaPj: String, idAlamat: String, hargaTawaran : String) {
        val url = "https://kumowal.my.id/api/pesanan_buat.php"
        binding.loadingProgressBar.visibility = View.VISIBLE
        val postData = HashMap<String, String>()
        postData.put("jenis_pesanan","babi")
        postData.put("berat",berat)
        postData.put("bagian_potongan", bagianPotongan)
        postData.put("nama_pj", namaPj)
        postData.put("id_alamat", idAlamat)
        postData.put("userId",Pengguna.id.toString())
        postData.put("harga",hargaBabi.toString())
        postData.put("harga_tawaran",hargaTawaran )

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