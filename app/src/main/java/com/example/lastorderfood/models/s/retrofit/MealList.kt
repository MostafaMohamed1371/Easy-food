package com.example.lastorderfood.models.s.retrofit


import com.google.gson.annotations.SerializedName

data class MealList(
    @SerializedName("meals")
    val meals: List<Meal?>? = null
)