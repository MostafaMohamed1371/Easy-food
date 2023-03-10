package com.example.lastorderfood.models.s.retrofit


import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("meals")
    val meals: List<CategoryMeal>? = listOf()
)