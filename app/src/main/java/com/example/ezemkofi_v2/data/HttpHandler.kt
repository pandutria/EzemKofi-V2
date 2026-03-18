package com.example.ezemkofi_v2.data

import com.example.ezemkofi_v2.data.model.HttpModel
import com.example.ezemkofi_v2.util.Helper
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler {
    fun request(
        endpoint: String,
        method: String ?= "GET",
        token: String? = null,
        rBody: String? = null
    ): HttpModel {
        return  try {
            val conn = URL(Helper.url + endpoint).openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = method

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer ${token}")
            }

            if (rBody != null) {
                conn.doOutput = true
                conn.outputStream.use { it.write(rBody.toByteArray()) }
            }

            val code = conn.responseCode
            val body = try {
                conn.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                conn.errorStream.bufferedReader().use { it.readText() }
            }

            HttpModel(code, body)
        } catch (e: Exception) {
            HttpModel(500, e.message ?: "error")
        }
    }
}