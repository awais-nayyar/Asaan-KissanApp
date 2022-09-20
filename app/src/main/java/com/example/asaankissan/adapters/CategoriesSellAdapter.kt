package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutCategoriesSellBinding
import com.example.asaankissan.models.Category
import com.makeramen.roundedimageview.RoundedImageView

class CategoriesSellAdapter(
    private val context: Context?,
    private val dataset: ArrayList<Category>?
) : RecyclerView.Adapter<CategoriesSellAdapter.CategoriesSellHolder>(){


    // for onclick listner
    private lateinit var mListner: onItemClickListner

    interface onItemClickListner{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listenr : onItemClickListner){
        mListner = listenr
    }

     class CategoriesSellHolder(itemBinding: ItemLayoutCategoriesSellBinding, listner: onItemClickListner) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val tvCatName : TextView = itemBinding.tvCategories
        val rivCatImage : RoundedImageView = itemBinding.rivCategories
        val cardView = itemBinding.cardView

        init {
            cardView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesSellHolder {
        return CategoriesSellHolder(
            ItemLayoutCategoriesSellBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), mListner
        )
    }

    override fun onBindViewHolder(holder: CategoriesSellHolder, position: Int) {
        val currentCategory = dataset!![position]

        Glide.with(holder.itemView).load(currentCategory.catImage)
            .placeholder(R.drawable.placeholder).into(holder.rivCatImage)
        holder.tvCatName.setText(currentCategory.catName)


    }

    override fun getItemCount(): Int {
        return dataset!!.size
    }
}