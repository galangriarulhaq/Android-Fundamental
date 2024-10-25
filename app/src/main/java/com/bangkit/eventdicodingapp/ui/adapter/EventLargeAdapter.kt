package com.bangkit.eventdicodingapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.data.remote.response.ListEventsItem
import com.bangkit.eventdicodingapp.databinding.ItemEventLargeBinding
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class EventLargeAdapter(private val onItemClick: (Int) -> Unit, private val onFavoriteClick: (EventEntity) -> Unit): ListAdapter<EventEntity, EventLargeAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemEventLargeBinding, private val onItemClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: EventEntity){
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgEvent)
            binding.tvEvent.text = event.title
            binding.tvCategory.text = event.category
            binding.tvOwner.text = "Oleh : ${event.owner}"
            itemView.setOnClickListener {
                onItemClick(event.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        val btnFav = holder.itemView.findViewById<MaterialButton>(R.id.btn_fav)

        if (event.isFavorite) {
            btnFav.setIconResource(R.drawable.baseline_favorite_24)
        } else {
            btnFav.setIconResource(R.drawable.baseline_favorite_border_24)
        }

        btnFav.setOnClickListener {
            onFavoriteClick(event)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

}