package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutShowAllAdsBinding
import com.example.asaankissan.models.Ad
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class CategoriesAdsAdapter(private val context: Context?, private val dataset: ArrayList<Ad>) :
    RecyclerView.Adapter<CategoriesAdsAdapter.CategoriesAdsHolder>() {

    // for onclick listner
    private lateinit var mListner: onItemClickListner

    interface onItemClickListner {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listenr: onItemClickListner) {
        mListner = listenr
    }

    class CategoriesAdsHolder(
        adBinding: ItemLayoutShowAllAdsBinding, listenr: onItemClickListner
    ) : RecyclerView.ViewHolder(adBinding.root) {

        val ivAd: ImageView = adBinding.ivAd
        val tvAdTitle: TextView = adBinding.tvAdTitle
        val tvAdDescription: TextView = adBinding.tvAdDescription
        val tvAdPrice: TextView = adBinding.tvAdPrice
        val tvAdMeasure: TextView = adBinding.tvAdMeasure
        val tvAdLocation: TextView = adBinding.tvAdLocation
        val tvAdUploadTime: TextView = adBinding.tvAdUploadTime

        init {
            itemView.setOnClickListener {
               listenr.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdsHolder {
        return CategoriesAdsHolder(
            ItemLayoutShowAllAdsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), mListner
        )
    }

    override fun onBindViewHolder(holder: CategoriesAdsHolder, position: Int) {
        val currentAdsList = dataset[position]

        holder.tvAdTitle.text = currentAdsList.adTitle
        holder.tvAdDescription.text = currentAdsList.adDescription
        holder.tvAdPrice.text = "RS: " + currentAdsList.adPrice.toString()
        holder.tvAdMeasure.text = currentAdsList.adMeasure
        holder.tvAdLocation.text = currentAdsList.adLocation
        val pt : PrettyTime = PrettyTime()
        holder.tvAdUploadTime.text = pt.format(Date(currentAdsList.adUploadTime.toLong())).toString()

        Glide.with(holder.itemView).load(currentAdsList.adImage[0])
            .placeholder(R.drawable.placeholder)
            .into(holder.ivAd)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}