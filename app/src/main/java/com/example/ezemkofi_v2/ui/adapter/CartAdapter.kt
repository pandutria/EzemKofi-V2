package com.example.ezemkofi_v2.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezemkofi_v2.data.model.Cart
import com.example.ezemkofi_v2.data.model.Coffee
import com.example.ezemkofi_v2.databinding.ItemCartBinding
import com.example.ezemkofi_v2.databinding.ItemCoffeeBinding
import com.example.ezemkofi_v2.ui.CoffeeDetailActivity
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round

class CartAdapter(
    private val list: MutableList<Cart> = mutableListOf()
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = list[position]
        holder.apply {
            binding.tvName.text = cart.coffee.name
            binding.tvCategory.text = cart.coffee.category
            binding.tvSize.text = "Size: ${cart.size}"
            binding.tvPrice.text = (cart.coffee.price * cart.qty).toString()
            binding.tvQty.text = cart.qty.toString()

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = Helper.loadImage(cart.coffee.imagePath)
                binding.imgImage.setImageBitmap(bitmap)
            }

            binding.minus.setOnClickListener {
                if (cart.qty == 1) {
                   list.removeAt(position)
                    notifyDataSetChanged()
                    return@setOnClickListener
                }
                cart.qty--
                binding.tvQty.text = cart.qty.toString()
                binding.tvPrice.text = (cart.coffee.price * cart.qty).toString()
            }

            binding.plus.setOnClickListener {
                cart.qty++
                binding.tvQty.text = cart.qty.toString()
                binding.tvPrice.text = (cart.coffee.price * cart.qty).toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}