package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutUploadImagesBinding

class UploadImageAdapter(private val context: Context, private val dataset: ArrayList<String>) :
    RecyclerView.Adapter<UploadImageAdapter.UploadImageHolder>() {

    class UploadImageHolder(itemBinding: ItemLayoutUploadImagesBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        val ivImage: ImageView = itemBinding.ivImage
        val ivRemove: ImageView = itemBinding.ivRemove
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageHolder {
        return UploadImageHolder(
            ItemLayoutUploadImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UploadImageHolder, position: Int) {

        val currentImageUrl = dataset[position]
        if (currentImageUrl.isNotEmpty()){

            Glide.with(holder.itemView).load(currentImageUrl)
                .placeholder(R.drawable.placeholder).into(holder.ivImage)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}