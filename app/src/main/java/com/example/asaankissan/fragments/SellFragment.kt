package com.example.asaankissan.fragments

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asaankissan.R
import com.example.asaankissan.activities.AdPostingActivity
import com.example.asaankissan.adapters.CategoriesSellAdapter
import com.example.asaankissan.databinding.FragmentSellBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Category
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SellFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SellFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //variable declaration starting from here

    private var binding: FragmentSellBinding? = null
    private var categoriesList: ArrayList<Category>? = null
    private lateinit var auth: FirebaseAuth


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
        binding = FragmentSellBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing array List
        categoriesList = ArrayList()
        //setUpCategoriesSellAdapter()

        auth = FirebaseAuth.getInstance()
        FirebaseClass().getAllCategories(this)

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
    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    fun loadCategoriesIntoRecyclerView(catList : ArrayList<Category>) {
        categoriesList?.addAll(catList)

        binding?.rvCategories?.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        val categoriesAdapter = CategoriesSellAdapter(context, categoriesList!!)
        binding?.rvCategories?.adapter = categoriesAdapter

        hideShimmer()
        categoriesAdapter.setOnItemClickListner(object : CategoriesSellAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                val selectedCategory = categoriesList!![position]
                val categoryIntent = Intent(requireContext(), AdPostingActivity::class.java)
                categoryIntent.putExtra("category", selectedCategory)
                startActivity(categoryIntent)
            }
        })


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
         * @return A new instance of fragment SellFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SellFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}