package com.example.ezemkofi_v2.data.local

import android.content.Context

class TokenManager(context: Context) {
    private val pref = "pref"
    private val key = "key"

    private val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)

    fun save(token: String) {
        shared.edit().putString(key, token).apply()
    }

    fun get(): String? {
        return shared.getString(key, null)
    }

    fun remove() {
        shared.edit().remove(key).apply()
    }
}