package com.bangkit.eventdicodingapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.remote.response.ListEventsItem
import com.bangkit.eventdicodingapp.databinding.ItemEventLargeBinding
import com.bumptech.glide.Glide

class EventSearchAdapter(private val onItemClick: (Int) -> Unit): ListAdapter<ListEventsItem, EventSearchAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemEventLargeBinding, private val onItemClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem){
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgEvent)
            binding.tvEvent.text = event.name
            binding.tvCategory.text = event.category
            binding.tvOwner.text = "Oleh : ${event.ownerName}"
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemEventLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivFav = holder.itemView.findViewById<ImageView>(R.id.iv_fav)

        ivFav.visibility = View.GONE
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}