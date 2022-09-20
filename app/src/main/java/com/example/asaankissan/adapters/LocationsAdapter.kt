package com.example.asaankissan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaankissan.databinding.ItemLayoutLocationsListBinding
import com.example.asaankissan.models.City

class LocationsAdapter(
    private val context: Context, private val dataset: ArrayList<City>
) : RecyclerView.Adapter<LocationsAdapter.LocationsHolder>() {

    private lateinit var mListner : onItemClickListner

    interface onItemClickListner{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListner(listner : onItemClickListner) {
        mListner = listner
    }

    inner class LocationsHolder(locationBinding: ItemLayoutLocationsListBinding,
                                listner : onItemClickListner) :
        RecyclerView.ViewHolder(locationBinding.root) {

        val tvLocations: TextView = locationBinding.tvLocations

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsHolder {
        return LocationsHolder(
            ItemLayoutLocationsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListner
        )
    }

    override fun onBindViewHolder(holder: LocationsHolder, position: Int) {
        val currentLocation = dataset[position]
        holder.tvLocations.text = currentLocation.cityName
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}