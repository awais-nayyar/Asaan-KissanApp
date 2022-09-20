package com.example.asaankissan.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asaankissan.activities.AdPostingActivity
import com.example.asaankissan.activities.AdSearchActivity
import com.example.asaankissan.activities.AdsDetailActivity
import com.example.asaankissan.activities.CategoryAdsActivity
import com.example.asaankissan.adapters.CategoriesExploreAdapter
import com.example.asaankissan.adapters.FreshRecomAdapter
import com.example.asaankissan.adapters.ImageSliderAdapter
import com.example.asaankissan.databinding.FragmentExploreBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Category
import com.example.asaankissan.models.Ad
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //variables declaration starting from here
    private var binding: FragmentExploreBinding? = null
    private var categoriesList: ArrayList<Category>? = null
    private var itemsList: ArrayList<Ad>? = null
    private lateinit var auth : FirebaseAuth

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
        binding = FragmentExploreBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setting up custom tool bar
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).title = ""

        // initializing arraylist
        categoriesList = ArrayList()
        itemsList = ArrayList()

        // setting searchBar

        binding?.tvSearchBar?.setOnClickListener{
            startActivity(Intent(context, AdSearchActivity::class.java))
        }

        // calling my functions
        setUpImageSlider()


        auth = FirebaseAuth.getInstance()
        FirebaseClass().getAllFreshRecommAds(this)
        FirebaseClass().getAllCategories(this)

        //shimmer effect
        showShimmer()

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

     fun loadFreshRecommAdsInToRecyclerView(adsList : ArrayList<Ad>) {

        //itemsList?.addAll(adsList)
         for (ad in adsList){
             if (ad.adStatus.equals("1")){
                 itemsList?.add(ad)
             }
         }
         if(itemsList?.size == 0){
             binding?.tvNoAds?.visibility = View.VISIBLE
         }else{
             binding?.tvNoAds?.visibility = View.INVISIBLE
         }
        binding?.rvFreshRecom?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        val freshRecomAdapter = FreshRecomAdapter(context, itemsList!!)
        binding?.rvFreshRecom?.adapter = freshRecomAdapter

        freshRecomAdapter.setOnItemClickListner(object : FreshRecomAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                val selectedAd = itemsList!![position]
                val freshRecommIntent = Intent(context, AdsDetailActivity::class.java)
                freshRecommIntent.putExtra("ad", selectedAd)
                startActivity(freshRecommIntent)
            }

        })
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
     fun loadCategoriesIntoRecyclerView(catList : ArrayList<Category>) {
         hideShimmer()
        categoriesList?.addAll(catList)

        binding?.rvCategories?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val categoriesAdapter = CategoriesExploreAdapter(context, categoriesList!!)
        binding?.rvCategories?.adapter = categoriesAdapter

        // calling onclick listner of adapter
        categoriesAdapter.setOnItemClickListner(object : CategoriesExploreAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {

                val selectCategory = categoriesList!![position]
                val categoryIntent = Intent(context, CategoryAdsActivity::class.java)
                categoryIntent.putExtra("category", selectCategory)
                startActivity(categoryIntent)

            }

        })

    }

    private fun setUpImageSlider() {

        val sliderAdapter = ImageSliderAdapter(context, Constants.getImageListSlider())
        binding?.imageSlider?.setSliderAdapter(sliderAdapter)
        binding?.imageSlider?.setIndicatorAnimation(IndicatorAnimationType.SWAP)
        binding?.imageSlider?.setSliderTransformAnimation(SliderAnimations.CUBEOUTDEPTHTRANSFORMATION)
        binding?.imageSlider?.startAutoCycle()
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
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}