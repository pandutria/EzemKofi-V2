package com.example.ezemkofi_v2.ui

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.local.CartManager
import com.example.ezemkofi_v2.data.model.Cart
import com.example.ezemkofi_v2.data.model.Coffee
import com.example.ezemkofi_v2.databinding.ActivityCoffeeDetailBinding
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.math.round

class CoffeeDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCoffeeDetailBinding? = null
    private val binding get() = _binding!!

    var coffee: Coffee? = null
    var qty = 0
    var size = "S"
    var price = 0.0
    val widhtAndHeight = 700
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityCoffeeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            finish()
        }

        detail(intent.getIntExtra("id", 0))

        binding.minus.setOnClickListener {
            if (qty == 0) {
                Helper.toast(this, "Qty must be minimal 1")
                return@setOnClickListener
            }
            qty--
            binding.tvQty.text = qty.toString()
        }

        binding.plus.setOnClickListener {
            qty++
            binding.tvQty.text = qty.toString()
        }

        binding.tvS.setOnClickListener {
            size = "S"
            binding.tvPrice.text = (round((price * 0.85) * 100) / 100).toString()
            checkSize()
        }

        binding.tvM.setOnClickListener {
            size = "M"
            binding.tvPrice.text = price.toString()
            checkSize()
        }

        binding.tvL.setOnClickListener {
            size = "L"
            binding.tvPrice.text = (round((price * 1.15) * 100) / 100).toString()
            checkSize()
        }

        binding.btn.setOnClickListener {
            if (qty == 0) {
                Helper.toast(this, "Qty must be minimal 1")
                return@setOnClickListener
            }

            for (i in CartManager.cart) {
                if (i.coffee.id == coffee?.id && i.size == size) {
                    i.qty += qty
                    return@setOnClickListener
                }
            }

            CartManager.cart.add(
                Cart(
                    coffee!!,
                    size,
                    qty,
                )
            )
        }
    }

    fun checkSize() {
        when(size) {
            "S" -> {
                binding.tvS.setBackgroundResource(R.drawable.bg_size_selected)
                binding.tvM.setBackgroundResource(R.drawable.bg_size_unselected)
                binding.tvL.setBackgroundResource(R.drawable.bg_size_unselected)

                binding.tvS.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvM.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvL.setTextColor(ContextCompat.getColor(this, R.color.green))

                binding.imgImage.animate()
                    .rotationBy(360f)
                    .setDuration(3000)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        binding.imgImage.layoutParams.width = (widhtAndHeight * 0.85).toInt()
                        binding.imgImage.layoutParams.height = (widhtAndHeight * 0.85).toInt()
                        binding.imgImage.requestLayout()
                    }
                    .start()
            }
            "M" -> {
                binding.tvS.setBackgroundResource(R.drawable.bg_size_unselected)
                binding.tvM.setBackgroundResource(R.drawable.bg_size_selected)
                binding.tvL.setBackgroundResource(R.drawable.bg_size_unselected)

                binding.tvS.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvM.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.tvL.setTextColor(ContextCompat.getColor(this, R.color.green))

                binding.imgImage.animate()
                    .rotationBy(360f)
                    .setDuration(3000)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        binding.imgImage.layoutParams.width = (widhtAndHeight * 1.00).toInt()
                        binding.imgImage.layoutParams.height = (widhtAndHeight * 1.00).toInt()
                        binding.imgImage.requestLayout()
                    }
                    .start()
            }
            "L" -> {
                binding.tvS.setBackgroundResource(R.drawable.bg_size_unselected)
                binding.tvM.setBackgroundResource(R.drawable.bg_size_unselected)
                binding.tvL.setBackgroundResource(R.drawable.bg_size_selected)

                binding.tvS.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvM.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvL.setTextColor(ContextCompat.getColor(this, R.color.white))

                binding.imgImage.animate()
                    .rotationBy(360f)
                    .setDuration(3000)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction {
                        binding.imgImage.layoutParams.width = (widhtAndHeight * 1.15).toInt()
                        binding.imgImage.layoutParams.height = (widhtAndHeight * 1.15).toInt()
                        binding.imgImage.requestLayout()
                    }
                    .start()
            }
        }

    }

    fun detail(id: Int) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "coffee/${id}"
                )
            }

            if (result.code in 200..300) {
                val data = JSONObject(result.body)

                coffee = Coffee(
                    data.getInt("id"),
                    data.getString("name"),
                    data.getDouble("rating"),
                    data.getDouble("price"),
                    data.getString("imagePath"),
                    data.getString("category"),
                    data.getString("description")
                )

                binding.tvName.text = coffee?.name
                price = coffee?.price!!
                binding.tvPrice.text = (round((price * 0.85) * 100) / 100).toString()
                binding.tvDesc.text = coffee?.description
                binding.tvRating.text = coffee?.rating.toString()

                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = Helper.loadImage(coffee?.imagePath!!)
                    binding.imgImage.setImageBitmap(bitmap)
                }

                checkSize()
            }
        }
    }
}