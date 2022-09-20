package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutFreshRecomendationBinding
import com.example.asaankissan.models.Ad

class FreshRecomAdapter(private val context: Context?, private val dataset: ArrayList<Ad>)
    : RecyclerView.Adapter<FreshRecomAdapter.FreshRecomHolder>() {

    // for onclick listner
    private lateinit var mListner: onItemClickListner

    interface onItemClickListner{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listenr : onItemClickListner){
        mListner = listenr
    }

    class FreshRecomHolder(adBinding: ItemLayoutFreshRecomendationBinding, listner: onItemClickListner)
        : RecyclerView.ViewHolder(adBinding.root) {

        val ivProductFresh : ImageView = adBinding.ivProductFresh
        val tvProductFreshTitle : TextView = adBinding.tvProductNameFresh
        val tvProductFreshDescription : TextView = adBinding.tvProductDescriptionFresh
        val tvProductFreshPrice : TextView = adBinding.tvProductPriceFresh
        val tvProductFreshLocation : TextView = adBinding.tvProductLocationFresh
        val tvAdMeasure : TextView = adBinding.tvAdMeasure

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreshRecomHolder {

        return FreshRecomHolder(
            ItemLayoutFreshRecomendationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), mListner
        )
    }

    override fun onBindViewHolder(holder: FreshRecomHolder, position: Int) {

        val currentItem = dataset[position]

        Glide.with(holder.itemView).load(currentItem.adImage[0])
            .placeholder(R.drawable.placeholder).into(holder.ivProductFresh)
        holder.tvProductFreshTitle.text = currentItem.adTitle
        holder.tvProductFreshDescription.text = currentItem.adDescription
        holder.tvProductFreshPrice.text = "RS: " + currentItem.adPrice.toString()
        holder.tvProductFreshLocation.text = currentItem.adLocation
        holder.tvAdMeasure.text = currentItem.adMeasure
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}