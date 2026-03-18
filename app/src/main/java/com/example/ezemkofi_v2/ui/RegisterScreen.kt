package com.example.ezemkofi_v2.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ezemkofi_v2.R
import com.example.ezemkofi_v2.data.HttpHandler
import com.example.ezemkofi_v2.data.local.TokenManager
import com.example.ezemkofi_v2.databinding.ActivityRegisterScreenBinding
import com.example.ezemkofi_v2.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RegisterScreen : AppCompatActivity() {
    private var _binding: ActivityRegisterScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
        }

        binding.btn.setOnClickListener {
            if (binding.etUsername.text.isEmpty() || binding.etPassword.text.isEmpty()
                || binding.etEmail.text.isEmpty() || binding.etFullname.text.isEmpty()) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }

            if (binding.etPassword.text.length < 4) {
                Helper.toast(this, "Password should have minimal four characters.")
                return@setOnClickListener
            }

            if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                Helper.toast(this, "Confirm Password and Password must be same")
                return@setOnClickListener
            }

            register()
        }
    }
    fun register() {
        val rBody = JSONObject().apply {
            put("username", binding.etUsername.text.toString())
            put("fullname", binding.etFullname.text.toString())
            put("email", binding.etEmail.text.toString())
            put("password", binding.etPassword.text.toString())
        }

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "register",
                    "POST",
                    rBody = rBody.toString()
                )
            }

            if (result.code in 200..300) {
                TokenManager(this@RegisterScreen).save(result.body)
                Helper.toast(this@RegisterScreen, "Register success")
                startActivity(Intent(this@RegisterScreen, MainScreen::class.java))
            } else {
                Helper.toast(this@RegisterScreen, result.body)
            }
        }
    }
}