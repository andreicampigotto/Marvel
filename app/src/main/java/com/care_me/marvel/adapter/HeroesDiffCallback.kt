package com.care_me.marvel.adapter

import androidx.recyclerview.widget.DiffUtil
import com.care_me.marvel.model.Hero

class HeroesDiffCallback : DiffUtil.ItemCallback<Hero>() {
    override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
        return oldItem.id == newItem.id
    }
}