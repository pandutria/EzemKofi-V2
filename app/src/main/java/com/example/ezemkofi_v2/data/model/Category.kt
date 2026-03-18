package com.example.ezemkofi_v2.data.model

data class Category (
    val id: Int,
    val name: String,
    var isSelected: Boolean? = false
)