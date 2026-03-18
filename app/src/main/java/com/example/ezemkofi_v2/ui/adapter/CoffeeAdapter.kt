package com.example.ezemkofi_v2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofi_v2.data.model.Coffee
import com.example.ezemkofi_v2.databinding.ItemCoffeeBinding
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoffeeAdapter(
    private val list: MutableList<Coffee> = mutableListOf()
): RecyclerView.Adapter<CoffeeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCoffeeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoffeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coffee = list[position]
        holder.apply {
            binding.tvName.text = coffee.name
            binding.tvCategory.text = coffee.category
            binding.tvPrice.text = "$${coffee.price}"
            binding.tvRating.text = coffee.rating.toString()

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = Helper.loadImage(coffee.imagePath)
                binding.imgImage.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}