package com.example.asaankissan.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asaankissan.R
import com.example.asaankissan.adapters.CategoriesAdsAdapter
import com.example.asaankissan.adapters.MyAdsAdapter
import com.example.asaankissan.databinding.ActivityCategoryAdsBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Ad
import com.example.asaankissan.models.Category
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth

class CategoryAdsActivity : AppCompatActivity() {
    var binding : ActivityCategoryAdsBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var getCategory : Category
    private lateinit var catType: String
    private var adsList : ArrayList<Ad>? = null
    private var myAdsAdapter : MyAdsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityCategoryAdsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //getting category intent which sent from Explore fragment
        getCategory = intent.getSerializableExtra("category") as Category
        catType = getCategory.catName

        // setting toolbar name
        binding?.tvToolBar?.text = catType

        //initializing adsList
        adsList = ArrayList()

        auth = FirebaseAuth.getInstance()
        FirebaseClass().getCurrentCategoryAds(this, catType)

        //show shimmer
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

    fun loadCurrentCategoryAdsListIntoRecyclerView(list : ArrayList<Ad>) {

        //adsList?.addAll(list)
        for (ad in list){
            if (ad.adStatus.equals("1")){
                adsList?.add(ad)
            }
        }
        if (adsList?.size == 0){
            binding?.tvNoAds?.visibility = View.VISIBLE
        }else{
           binding?.tvNoAds?.visibility = View.INVISIBLE
        }
        binding?.rvCategoryAds?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val categoryAdsAdapter = CategoriesAdsAdapter(this, adsList!!)
        binding?.rvCategoryAds?.adapter = categoryAdsAdapter

        hideShimmer()

        categoryAdsAdapter.setOnItemClickListner(object : CategoriesAdsAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                val selectedAd = adsList!![position]
                val catIntent = Intent(this@CategoryAdsActivity, AdsDetailActivity::class.java)
                catIntent.putExtra("ad", selectedAd)
                startActivity(catIntent)
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onRestart() {
        super.onRestart()
        auth = FirebaseAuth.getInstance()
        FirebaseClass().getCurrentCategoryAds(this, catType)
        FirebaseClass().getCurrentUserDataForActvity(this)
    }

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}