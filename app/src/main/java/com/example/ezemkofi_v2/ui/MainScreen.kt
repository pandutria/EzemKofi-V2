package com.example.ezemkofi_v2.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.local.AuthSession
import com.example.ezemkofi_v2.data.local.TokenManager
import com.example.ezemkofi_v2.data.model.Category
import com.example.ezemkofi_v2.data.model.User
import com.example.ezemkofi_v2.databinding.ActivityMainScreenBinding
import com.example.ezemkofi_v2.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class MainScreen : AppCompatActivity() {
    private var _binding: ActivityMainScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        me()
        category()
    }

    fun category() {
        val categoryList: MutableList<Category> = mutableListOf()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "coffee-category"
                )
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)

                    categoryList.add(
                        Category(
                            data.getInt("id"),
                            data.getString("name"),
                            false
                        )
                    )
                }

                binding.rvCategory.adapter = CategoryAdapter(categoryList)
            }
        }
    }

    fun me() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me",
                    token = TokenManager(this@MainScreen).get()
                )
            }

            if (result.code in 200..300) {
                val data = JSONObject(result.body)

                val user = User(
                    data.getInt("id"),
                    data.getString("username"),
                    data.getString("fullName"),
                    data.getString("email")
                )

                AuthSession.user = user
                binding.tvFullname.text = AuthSession.user.fullName
            }
        }
    }

    override fun onResume() {
        super.onResume()
        me()
    }
}