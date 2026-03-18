package com.example.ezemkofi_v2.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.local.TokenManager
import com.example.ezemkofi_v2.databinding.ActivityLoginScreenBinding
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginScreen : AppCompatActivity() {
    private var _binding: ActivityLoginScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        binding.etUsername.setText("string")
        binding.etPassword.setText("string")

        binding.btn.setOnClickListener {
            if (binding.etUsername.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }
            login()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginScreen, RegisterScreen::class.java))
        }
    }

    fun login() {
        val rBody = JSONObject().apply {
            put("username", binding.etUsername.text.toString())
            put("password", binding.etPassword.text.toString())
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "auth",
                    "POST",
                    rBody = rBody.toString()
                )
            }

            if (result.code in 200..300) {
                TokenManager(this@LoginScreen).save(result.body)
                Helper.toast(this@LoginScreen, "Login success")
                startActivity(Intent(this@LoginScreen, MainScreen::class.java))
            } else {
                Helper.toast(this@LoginScreen, result.body)
            }
        }
    }
}