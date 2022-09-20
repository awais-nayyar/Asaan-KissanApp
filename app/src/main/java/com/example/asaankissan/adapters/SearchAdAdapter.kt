/*
package com.example.asaankissan.adapters

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asaankissan.R
import com.example.asaankissan.models.Ad
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class MemberAdapter(val context : Context, dataset: ArrayList<Ad>) :
    RecyclerView.Adapter<MemberAdapter.MemberHolder>() {

    private val dataset: ArrayList<Ad> = dataset

    private val backupDataset: ArrayList<Ad>
    private val clickListener: OnItemClickListener

    private var query: String? = null

    fun filter(query: String) {
        this.query = query
        dataset.clear()
        if (query.isEmpty()) {
            dataset.addAll(backupDataset)
        } else {
            for (i in backupDataset.indices) {
                val obj: Ad = backupDataset[i]
                if (obj.adTitle.toLowerCase()
                        .contains(query.toLowerCase()) || obj.adPrice.toString().toLowerCase()
                        .contains(query.toLowerCase()) || obj.adDescription.toLowerCase()
                        .contains(query.toLowerCase()) || obj.adAddress.toLowerCase()
                        .contains(query.toLowerCase())
                ) {
                    dataset.add(obj)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): com.example.easylist.MemberAdapter.MemberHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_member, parent, false)
        return com.example.easylist.MemberAdapter.MemberHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: com.example.easylist.MemberAdapter.MemberHolder,
        position: Int
    ) {
        val currentMember: Member = dataset[position]
        val spannableName = SpannableString(currentMember.getName())
        val spannablePhone = SpannableString(currentMember.getPhoneNumber())
        val spannableAmount = SpannableString(java.lang.String.valueOf(currentMember.getAmount()))
        if (query != null && !query!!.isEmpty()) {
            if (currentMember.getName().toLowerCase().contains(query!!.toLowerCase())) {
                val start: Int =
                    currentMember.getName().toLowerCase().indexOf(query!!.toLowerCase())
                val end = start + query!!.length
                spannableName.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannableName.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            if (currentMember.getPhoneNumber().contains(query)) {
                val start: Int = currentMember.getPhoneNumber().indexOf(query)
                val end = start + query!!.length
                spannablePhone.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannablePhone.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            if (java.lang.String.valueOf(currentMember.getAmount()).contains(query)) {
                val start: Int = java.lang.String.valueOf(currentMember.getAmount()).indexOf(query)
                val end = start + query!!.length
                spannableAmount.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannableAmount.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
        holder.tvName.setText(spannableName)
        holder.tvPhoneNumber.setText(spannablePhone)
        holder.tvAmount.setText("Added: $spannableAmount rs")
        val pt = PrettyTime()
        holder.tvRegistrationTime.setText(pt.format(Date(currentMember.getRegistrationTime())))
        holder.itemView.setOnClickListener(View.OnClickListener {
            clickListener.onItemClick(
                null,
                holder.itemView,
                holder.getAdapterPosition(),
                0
            )
        })
        holder.itemView.setOnLongClickListener(OnLongClickListener {
            if (longClickListener != null) {
                longClickListener!!.onItemLongClick(
                    null,
                    holder.itemView,
                    holder.getAdapterPosition(),
                    0
                )
            } else {
                false
            }
        })
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class MemberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvPhoneNumber: TextView
        var tvAmount: TextView
        var tvRegistrationTime: TextView

        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvRegistrationTime = itemView.findViewById(R.id.tvRegistrationTime)
        }
    }

    init {
        this.dataset = dataset
        backupDataset = ArrayList<Any?>(dataset)
        this.clickListener = clickListener
    }
}*/
