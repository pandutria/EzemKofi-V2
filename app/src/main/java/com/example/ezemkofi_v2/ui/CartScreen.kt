package com.example.ezemkofi_v2.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.local.CartManager
import com.example.ezemkofi_v2.data.local.TokenManager
import com.example.ezemkofi_v2.databinding.ActivityCartScreenBinding
import com.example.ezemkofi_v2.ui.adapter.CartAdapter
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class CartScreen : AppCompatActivity() {
    private var _binding: ActivityCartScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityCartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.rvCart.adapter = CartAdapter(CartManager.cart)

        binding.btn.setOnClickListener {
            if (CartManager.cart.isEmpty()) {
                Helper.toast(this, "Cart must be minimal 1")
                return@setOnClickListener
            }

            checkout()
        }
    }

    fun checkout() {
        lifecycleScope.launch {
            val rBody = JSONArray()

            for (i in CartManager.cart) {
                val json = JSONObject().apply {
                    put("coffeeId", i.coffee.id)
                    put("size", i.size)
                    put("qty", i.qty)
                }
                rBody.put(json)
            }

            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "checkout",
                    "POST",
                    TokenManager(this@CartScreen).get(),
                    rBody.toString()
                )
            }

            if (result.code in 200..300) {
                CartManager.cart.clear()
                finish()

            } else {
                Helper.toast(this@CartScreen, result.body)
            }
        }
    }
}