package com.example.lastorderfood.models.s.retrofit


import com.google.gson.annotations.SerializedName

data class CategoryItems(
    @SerializedName("categories")
    val categories: List<Category?>? = null
)