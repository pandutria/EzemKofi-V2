package com.example.ezemkofi_v2.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.model.Category
import com.example.ezemkofi_v2.databinding.ItemCategoryBinding
import com.example.ezemkofi_v2.ui.MainScreen

class CategoryAdapter(
    private val list: MutableList<Category> = mutableListOf()
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = list[position]
        holder.apply {
            binding.tvName.text = category.name

            binding.root.setOnClickListener {
                for (i in list) {
                    i.isSelected = false
                }
                if (category != list[0]) {
                    list[0].isSelected = false
                } else {
                    list[0].isSelected = true
                }
                category.isSelected = true
                ((binding.root.context as MainScreen).coffeeByCategory(category.id))
                notifyDataSetChanged()
            }

            if (category.isSelected == true) {
                binding.tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                binding.tvName.setBackgroundResource(R.drawable.bg_category_selected)
            } else {
                binding.tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.tvName.setBackgroundResource(R.drawable.bg_category_unselected)
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}