package com.example.lastorderfood.models.s.retrofit


import com.google.gson.annotations.SerializedName

data class CategoryMeal(
    @SerializedName("idMeal")
    val idMeal: String? = null,
    @SerializedName("strMeal")
    val strMeal: String? = null,
    @SerializedName("strMealThumb")
    val strMealThumb: String? = null
)