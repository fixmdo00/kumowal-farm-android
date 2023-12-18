package com.example.bat.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.bat.BuatPesananBabiActivity
import com.example.bat.PilihAlamatActivity
import com.example.bat.R
import com.example.bat.dataClass.Alamat

class PilihAlamatAdapater (private val productList:ArrayList<Alamat>, val lifecycleOwner : LifecycleOwner, val loadingBar : ProgressBar, val context : Context) : RecyclerView.Adapter<PilihAlamatAdapater.ProductsViewHolder>() {

    var list = productList //kalau mau gunakan notifyDataSetChanged, yang di ubah variabel ini

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pilih_alamat,parent,false)
        return ProductsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem = list[position]
        Log.d("alamat item", currentItem.toString())
        val alamat = currentItem.jalan + " " + currentItem.kecamatan + " " + currentItem.kabupaten + " " + currentItem.provinsi
        holder.tvJenisAlamat.text = currentItem.jenis
        holder.tvAlamat.text = alamat
        holder.btnPilih.setOnClickListener {
//            val intent = Intent(context, BuatPesananBabiActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            intent.putExtra("idAlamat", currentItem.id)
//            context.startActivity(intent)
        }
    }

    class ProductsViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvJenisAlamat : TextView = itemView.findViewById(R.id.itemPilihAlamatJenis)
        val tvAlamat : TextView = itemView.findViewById(R.id.itemPilihAlamatAlamat)
        val btnPilih : Button = itemView.findViewById(R.id.itemPesananBtnPilih)
    }
}