package com.example.lastorderfood.models.s.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandamMeal():Call<MealList>
    @GET("lookup.php?")
    fun getMealDetails(@Query("i")id:String):Call<MealList>
    @GET("filter.php?")
    fun getPopularItem(@Query("c")categoryName:String):Call<CategoryList>
    @GET("categories.php")
    fun getCategories():Call<CategoryItems>
    @GET("filter.php")
    fun getMealsByCategory(@Query("c")categoryName:String):Call<CategoryList>
    @GET("search.php")
    fun getSearchMeals(@Query("s")search:String):Call<MealList>

}