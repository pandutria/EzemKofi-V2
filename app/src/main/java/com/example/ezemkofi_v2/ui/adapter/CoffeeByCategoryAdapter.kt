package com.example.ezemkofi_v2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofi_v2.data.model.Coffee
import com.example.ezemkofi_v2.databinding.ItemCoffeeByCategoryBinding
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoffeeByCategoryAdapter(
    private val list: MutableList<Coffee> = mutableListOf()
): RecyclerView.Adapter<CoffeeByCategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCoffeeByCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoffeeByCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coffee = list[position]
        holder.apply {
            binding.tvName.text = coffee.name
            binding.tvRating.text = coffee.rating.toString()
            binding.tvPrice.text = coffee.price.toString()

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