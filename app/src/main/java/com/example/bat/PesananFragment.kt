package com.example.bat

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bat.adapter.PesananAdapater
import com.example.bat.dataClass.PesananData
import com.example.bat.databinding.FragmentPesananBinding
import com.example.bat.databinding.FragmentProfilBinding
import com.example.bat.`object`.Pesanan

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PesananFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PesananFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var _binding : FragmentPesananBinding
    private val binding get() = _binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var pesananAdapter : PesananAdapater
    private var pesananArrayList = ArrayList<PesananData>()

    private lateinit var colorSelected : ColorDrawable
    private lateinit var colorNotSelected : ColorDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPesananBinding.inflate(layoutInflater, container, false)
        Pesanan.getFromDB(binding.loadingProgressBar){}

        val color1 = ContextCompat.getColor(requireContext(), R.color.primaryBackground)
        val color2 = ContextCompat.getColor(requireContext(), R.color.white)
        colorSelected = ColorDrawable(color2)
        colorNotSelected = ColorDrawable(color1)

        recyclerView = binding.RVPesanan
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        pesananAdapter = PesananAdapater(pesananArrayList, this, binding.loadingProgressBar, requireContext())
        recyclerView.adapter = pesananAdapter
        Pesanan.livePesanan.observe(viewLifecycleOwner){
            pesananAdapter.list = Pesanan.getPesananArrayListByStatus("1")
            if(pesananAdapter.list.size == 0){
                binding.tvPesananKosong.visibility = View.VISIBLE
            } else {
                binding.tvPesananKosong.visibility = View.INVISIBLE
            }
            pesananAdapter.notifyDataSetChanged()
            select(binding.topBtn1,binding.topBtn1Tv)
            unselect(binding.topBtn2, binding.topBtn2Tv)
            unselect(binding.topBtn3, binding.topBtn3Tv)
            unselect(binding.topBtn4, binding.topBtn4Tv)
        }

        binding.topBtn1.setOnClickListener {
            pesananAdapter.list = Pesanan.getPesananArrayListByStatus("1")
            if(pesananAdapter.list.size == 0){
                binding.tvPesananKosong.visibility = View.VISIBLE
            } else {
                binding.tvPesananKosong.visibility = View.INVISIBLE
            }
            pesananAdapter.notifyDataSetChanged()
            select(binding.topBtn1,binding.topBtn1Tv)
            unselect(binding.topBtn2, binding.topBtn2Tv)
            unselect(binding.topBtn3, binding.topBtn3Tv)
            unselect(binding.topBtn4, binding.topBtn4Tv)

        }
        binding.topBtn2.setOnClickListener {
            pesananAdapter.list = Pesanan.getPesananArrayListByStatus("2")
            if(pesananAdapter.list.size == 0){
                binding.tvPesananKosong.visibility = View.VISIBLE
            } else {
                binding.tvPesananKosong.visibility = View.INVISIBLE
            }
            pesananAdapter.notifyDataSetChanged()
            unselect(binding.topBtn1,binding.topBtn1Tv)
            select(binding.topBtn2, binding.topBtn2Tv)
            unselect(binding.topBtn3, binding.topBtn3Tv)
            unselect(binding.topBtn4, binding.topBtn4Tv)


        }
        binding.topBtn3.setOnClickListener {
            pesananAdapter.list = Pesanan.getPesananArrayListByStatus("3")
            if(pesananAdapter.list.size == 0){
                binding.tvPesananKosong.visibility = View.VISIBLE
            } else {
                binding.tvPesananKosong.visibility = View.INVISIBLE
            }
            pesananAdapter.notifyDataSetChanged()
            unselect(binding.topBtn1,binding.topBtn1Tv)
            unselect(binding.topBtn2, binding.topBtn2Tv)
            select(binding.topBtn3, binding.topBtn3Tv)
            unselect(binding.topBtn4, binding.topBtn4Tv)

        }
        binding.topBtn4.setOnClickListener {
            pesananAdapter.list = Pesanan.getPesananArrayListByStatus("4")
            if(pesananAdapter.list.size == 0){
                binding.tvPesananKosong.visibility = View.VISIBLE
            } else {
                binding.tvPesananKosong.visibility = View.INVISIBLE
            }
            pesananAdapter.notifyDataSetChanged()
            unselect(binding.topBtn1,binding.topBtn1Tv)
            unselect(binding.topBtn2, binding.topBtn2Tv)
            unselect(binding.topBtn3, binding.topBtn3Tv)
            select(binding.topBtn4, binding.topBtn4Tv)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            Pesanan.getFromDB(binding.loadingProgressBar){
                pesananAdapter.list = Pesanan.getPesananArrayListByStatus("1")
                if(pesananAdapter.list.size == 0){
                    binding.tvPesananKosong.visibility = View.VISIBLE
                } else {
                    binding.tvPesananKosong.visibility = View.INVISIBLE
                }
                pesananAdapter.notifyDataSetChanged()
                select(binding.topBtn1,binding.topBtn1Tv)
                unselect(binding.topBtn2, binding.topBtn2Tv)
                unselect(binding.topBtn3, binding.topBtn3Tv)
                unselect(binding.topBtn4, binding.topBtn4Tv)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        return binding.root
    }

    private fun select(view : LinearLayout, text : TextView){
        view.background = colorSelected
        text.setTypeface(null, Typeface.BOLD)
    }

    private fun unselect(view : LinearLayout, text : TextView){
        view.background = colorNotSelected
        text.setTypeface(null, Typeface.NORMAL)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PesananFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PesananFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}