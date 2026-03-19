package com.example.ezemkofi_v2.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.model.Coffee
import com.example.ezemkofi_v2.databinding.ActivitySearchScreenBinding
import com.example.ezemkofi_v2.ui.adapter.CoffeeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class SearchScreen : AppCompatActivity() {
    private var _binding: ActivitySearchScreenBinding? = null
    private val binding get() = _binding!!
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySearchScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            finish()
        }

        coffee()
        binding.etSearch.requestFocus()
        binding.etSearch.isKeyboardNavigationCluster = true

        binding.etSearch.addTextChangedListener {
            coffee()
        }
    }

    fun coffee() {
        val coffeeAdapter: MutableList<Coffee> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                if (binding.etSearch.text.isNotEmpty()) {
                    HttpHandler().request(
                        "coffee?search=${binding.etSearch.text.toString()}"
                    )
                } else {
                    HttpHandler().request(
                        "coffee"
                    )
                }
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)

                    coffeeAdapter.add(
                        Coffee(
                            data.getInt("id"),
                            data.getString("name"),
                            data.getDouble("rating"),
                            data.getDouble("price"),
                            data.getString("imagePath"),
                            data.getString("category")
                        )
                    )
                }

                binding.rvCoffee.adapter = CoffeeAdapter(coffeeAdapter)
            }
        }
    }
}