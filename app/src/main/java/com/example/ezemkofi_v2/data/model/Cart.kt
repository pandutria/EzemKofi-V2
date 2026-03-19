package com.example.ezemkofi_v2.data.model

data class Cart(
    val coffee: Coffee,
    val size: String,
    var qty: Int,
)
