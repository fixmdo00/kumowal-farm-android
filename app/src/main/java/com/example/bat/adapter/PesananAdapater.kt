package com.example.bat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.bat.DetailPesananActivity
import com.example.bat.R
import com.example.bat.dataClass.PesananData
import com.example.bat.`object`.SatuanProduk
import com.example.bat.`object`.StatusPesanan
import java.text.NumberFormat
import java.util.Locale

class PesananAdapater (private val productList:ArrayList<PesananData>, val lifecycleOwner : LifecycleOwner, val loadingBar : ProgressBar, val context : Context) : RecyclerView.Adapter<PesananAdapater.ProductsViewHolder>() {

    var onItemClick : ((Array<String>) -> Unit)? = null

    var list = productList //kalau mau gunakan notifyDataSetChanged, yang di ubah variabel ini

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan,parent,false)
        return ProductsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        var currentItem = list[position]
        val satuan = SatuanProduk.getSatuan(currentItem.jenis_produk)

        holder.tvWaktu.text = currentItem.waktu
        holder.tvJenisPesanan.text = currentItem.jenis_produk.capitalize()
        holder.tvJumlahProduk.text = currentItem.jumlah_produk.toString() + " " + satuan
        holder.tvStatus.text = StatusPesanan.getStatus(currentItem.status)

        val totalHargaPesanan: String
        if (currentItem.penawaran_harga != 0){
            totalHargaPesanan = "Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.penawaran_harga)
        } else {
            totalHargaPesanan = "Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(currentItem.harga_satuan * currentItem.jumlah_produk)
        }
        holder.tvTotalHarga.text = totalHargaPesanan
        holder.btnLihatDetail.setOnClickListener {
            val intent = Intent(context, DetailPesananActivity::class.java)
            intent.putExtra("id_pesanan", currentItem.id_pesanan)
            context.startActivity(intent)
        }
    }

    class ProductsViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvWaktu : TextView = itemView.findViewById(R.id.itemPesananWaktu)
        val tvStatus : TextView = itemView.findViewById(R.id.itemPesananStatus)
        val tvJenisPesanan : TextView = itemView.findViewById(R.id.itemPesananJenisPesanan)
        val tvJumlahProduk : TextView = itemView.findViewById(R.id.itemPesananJumlahProduk)
        val tvTotalHarga : TextView = itemView.findViewById(R.id.itemPesananTotalHargaPesanan)
        val btnLihatDetail : Button = itemView.findViewById(R.id.itemPesananBtnLihatDetail)
    }
}