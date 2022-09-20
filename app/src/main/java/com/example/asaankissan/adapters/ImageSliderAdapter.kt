package com.example.asaankissan.adapters

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.asaankissan.databinding.ItemLayoutImageSliderBinding
import com.example.asaankissan.others.Constants
import com.smarteist.autoimageslider.SliderViewAdapter


class ImageSliderAdapter(private val context: Context?, private val imageList: ArrayList<String>)
    : SliderViewAdapter<ImageSliderAdapter.ViewHolder>() {

    class ViewHolder(itemBinding: ItemLayoutImageSliderBinding)
        : SliderViewAdapter.ViewHolder(itemBinding.root){

            val ivImage : ImageView = itemBinding.ivSliderImage
        }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ImageSliderAdapter.ViewHolder {

        return ViewHolder(
            ItemLayoutImageSliderBinding.inflate(LayoutInflater.from(parent?.context)
                , parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ImageSliderAdapter.ViewHolder?, position: Int) {

        val currentImage = imageList[position]

        Glide.with(viewHolder!!.itemView).load(currentImage).into(viewHolder.ivImage)
    }

}