package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutCategoriesExploreBinding
import com.example.asaankissan.models.Category
import de.hdodenhof.circleimageview.CircleImageView

class CategoriesExploreAdapter(private val context: Context?, private val dataset: ArrayList<Category>) :
    RecyclerView.Adapter<CategoriesExploreAdapter.CategoriesHolder>() {

    // for onclick listner
    private lateinit var mListner: onItemClickListner

    interface onItemClickListner{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listenr : onItemClickListner){
        mListner = listenr
    }

    inner class CategoriesHolder(itemBinding: ItemLayoutCategoriesExploreBinding, listner: onItemClickListner)
        : RecyclerView.ViewHolder(itemBinding.root) {

            val ivCatImage : CircleImageView = itemBinding.ivCategories
            val tvCatName : TextView = itemBinding.tvCategories

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        return CategoriesHolder(
            ItemLayoutCategoriesExploreBinding.inflate(LayoutInflater.from(parent.context)
                , parent, false)
        , mListner)
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        val currentCategory = dataset[position]
        Glide.with(holder.itemView).load(currentCategory.catImage)
            .placeholder(R.drawable.placeholder).into(holder.ivCatImage)
        holder.tvCatName.text = currentCategory.catName

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}