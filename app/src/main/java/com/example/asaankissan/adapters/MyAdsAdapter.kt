package com.example.asaankissan.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ItemLayoutMyAdsBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.fragments.MyAdsFragment
import com.example.asaankissan.models.Ad
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class MyAdsAdapter(private val context: Context, private val fragment: Fragment, private val dataset: ArrayList<Ad>) :
    RecyclerView.Adapter<MyAdsAdapter.AdsHolder>() {

    // for onclick listner
    private lateinit var mListner: onItemClickListner

    interface onItemClickListner {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListner(listenr: onItemClickListner) {
        mListner = listenr
    }

    class AdsHolder(adBinding: ItemLayoutMyAdsBinding, listner: onItemClickListner) :
        RecyclerView.ViewHolder(adBinding.root) {

        val ivAd: ImageView = adBinding.ivAd
        val tvAdTitle: TextView = adBinding.tvAdTitle
        val tvAdDescription: TextView = adBinding.tvAdDescription
        val tvAdPrice: TextView = adBinding.tvAdPrice
        val tvAdLocation: TextView = adBinding.tvAdLocation
        val tvAdUploadTime: TextView = adBinding.tvAdUploadTime
        val tvAdMeasure: TextView = adBinding.tvAdMeasure
        val ivDeleteAd: ImageButton = adBinding.ivMyAdDelete

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsHolder {
        return AdsHolder(
            ItemLayoutMyAdsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), mListner
        )
    }

    override fun onBindViewHolder(holder: AdsHolder, position: Int) {
        val currentAd = dataset[position]

        Glide.with(holder.itemView).load(currentAd.adImage[0])
            .placeholder(R.drawable.placeholder).into(holder.ivAd)
        holder.tvAdTitle.text = currentAd.adTitle
        holder.tvAdDescription.text = currentAd.adDescription
        holder.tvAdPrice.text = "RS: " + currentAd.adPrice.toString()
        holder.tvAdLocation.text = currentAd.adLocation
        holder.tvAdMeasure.text = currentAd.adMeasure
        //holder.tvAdUploadTime.text = currentAd.getAdUploadTime()
        val pt: PrettyTime = PrettyTime()
        holder.tvAdUploadTime.text = pt.format(Date(currentAd.adUploadTime.toLong())).toString()


        holder.ivDeleteAd.setOnClickListener {
            //create a function in firebase

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Confirm")
            alertDialog.setMessage("Do you really want to delete this Ad?")
            alertDialog.setPositiveButton("Yes") {_, _, ->

                FirebaseClass().deleteMyAd(fragment, currentAd, position)
                dataset.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
            alertDialog.setNegativeButton("No", null)

            alertDialog.show()
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}