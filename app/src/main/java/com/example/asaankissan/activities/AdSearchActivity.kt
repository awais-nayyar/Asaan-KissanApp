package com.example.asaankissan.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asaankissan.R
import com.example.asaankissan.adapters.CategoriesAdsAdapter
import com.example.asaankissan.databinding.ActivityAdSearchBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Ad
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class AdSearchActivity : AppCompatActivity() {
    private var binding: ActivityAdSearchBinding? = null
    private var adapter: CategoriesAdsAdapter? = null
    private lateinit var adsList: ArrayList<Ad>
    private lateinit var backUpList: ArrayList<Ad>
    private lateinit var auth: FirebaseAuth

    override fun onRestart() {
        super.onRestart()
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdSearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // initializing arrayLists
        adsList = ArrayList()
        backUpList = ArrayList()

        // firebase class
        auth = FirebaseAuth.getInstance()
        FirebaseClass().getAllAdsForSearch(this)

        showShimmer()

    }

    private fun hideShimmer() {
        binding?.shimmerLayout?.visibility = View.INVISIBLE
        binding?.mainLayout?.visibility = View.VISIBLE
        binding?.shimmerLayout?.stopShimmer()
    }
    private fun showShimmer() {
        binding?.shimmerLayout?.visibility = View.VISIBLE
        binding?.mainLayout?.visibility = View.INVISIBLE

    }
    fun loadAllAdsIntoRecylerView(list : ArrayList<Ad>){

        for (ad in list){
            if (ad.adStatus.equals("1")){
                adsList.add(ad)
                backUpList.add(ad)
            }
        }

       // adsList.addAll(list)
        //backUpList.addAll(list)

        Log.i("mytag", adsList.toString())
        binding?.rvSearchAds?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = CategoriesAdsAdapter(this, adsList)
        binding?.rvSearchAds?.adapter = adapter

        adapter!!.setOnItemClickListner(object : CategoriesAdsAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                val selectedAd = adsList[position]
                val searchIntent = Intent(this@AdSearchActivity, AdsDetailActivity::class.java)
                searchIntent.putExtra("ad", selectedAd)
                startActivity(searchIntent)
            }

        })
        hideShimmer()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView =
            menu!!.findItem(R.id.actionSearch).actionView as androidx.appcompat.widget.SearchView

        searchView.setIconified(false);
        searchView.requestFocus();
        setTitle("")

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val query = newText.toLowerCase()

                //val spanableTitle : SpannableString = SpannableString(adsList[1].adTitle)
                if (query.isEmpty()) {
                    adsList.clear()
                    adsList.addAll(backUpList)
                    if (adsList != null) {

                        adapter!!.notifyDataSetChanged()
                    }
                } else {
                    adsList.clear()
                    for (item in backUpList) {
                        if (item.adTitle.toLowerCase()
                                .contains(query) || item.adDescription.toLowerCase()
                                .contains(query) || item.adAddress.toLowerCase()
                                .contains(query) || item.adPrice.toString().toLowerCase()
                                .contains(query) || item.adCategory.toLowerCase()
                                .contains(query)
                        ) {
                            adsList.add(item)
                        }
                    }

                    adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null){
            binding = null
        }
    }
}