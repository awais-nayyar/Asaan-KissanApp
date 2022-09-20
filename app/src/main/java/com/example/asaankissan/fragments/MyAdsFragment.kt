package com.example.asaankissan.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asaankissan.R
import com.example.asaankissan.activities.AdsDetailActivity
import com.example.asaankissan.adapters.MyAdsAdapter
import com.example.asaankissan.databinding.FragmentMyAdsBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Ad
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAdsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAdsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // variable declaration starts from here
    var binding: FragmentMyAdsBinding? = null
    var myAdsList: ArrayList<Ad>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var myAdsAdapter : MyAdsAdapter

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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_ads, container, false)
        binding = FragmentMyAdsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialization of Ads List
        myAdsList = ArrayList()
        //myAdsList = Constants.getAdsList()

        //setUpMyAdsAdapter()
        //setUpToolBar()

        auth = FirebaseAuth.getInstance()
        FirebaseClass().getMyAds(this)

        // shimmer effect

        showShimmer()
        binding?.tvNoAds?.visibility = View.INVISIBLE

    }

    private fun showShimmer() {
        binding?.shimmerLayout?.visibility = View.VISIBLE
        binding?.shimmerLayout?.startShimmer()
        binding?.mainLayout?.visibility = View.INVISIBLE
    }

    private fun hideShimmer() {
        binding?.shimmerLayout?.visibility = View.INVISIBLE
        binding?.shimmerLayout?.stopShimmer()
        binding?.mainLayout?.visibility = View.VISIBLE
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun loadMyAdsListIntoRecyclerView(ads : ArrayList<Ad>) {

        //myAdsList?.addAll(ads)
        for (ad in ads){
            if (ad.adStatus.equals("1")){
                myAdsList?.add(ad)
            }
        }
        if (myAdsList?.size == 0){
            binding?.tvNoAds?.visibility = View.VISIBLE
        }else{
            binding?.tvNoAds?.visibility = View.INVISIBLE
        }
        //showToast(myAdsList?.size.toString())
        binding?.rvMyAds?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        myAdsAdapter = MyAdsAdapter(requireContext(),this, myAdsList!!)
        binding?.rvMyAds?.adapter = myAdsAdapter

        hideShimmer()

        myAdsAdapter.setOnItemClickListner(object : MyAdsAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                val selectedAd = myAdsList!![position]

                val myAdsIntent = Intent(context, AdsDetailActivity::class.java)
                myAdsIntent.putExtra("ad", selectedAd)
                startActivity(myAdsIntent)
            }
        })

    }

    fun deleteConfirmDialog(){
        val alertDialog = AlertDialog.Builder(requireContext())
    }

    private fun setUpToolBar() {
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        if((activity as AppCompatActivity).supportActionBar != null){
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).title = "Ads Detail"
            (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding?.toolbar?.setNavigationOnClickListener{

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAdsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAdsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}