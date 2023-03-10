package com.example.lastorderfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastorderfood.models.s.retrofit.CategoryList
import com.example.lastorderfood.models.s.retrofit.CategoryMeal
import com.example.lastorderfood.models.s.retrofit.MealInstance
import retrofit2.Call
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    private var categoryMealsLiveData= MutableLiveData<List<CategoryMeal?>>()

    fun getMealsByCategory(categoryName:String){
        MealInstance.api.getMealsByCategory(categoryName).enqueue(object :retrofit2.Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categoryMealsLiveData.value= response.body()!!.meals!!
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })


    }

    fun observeCategoryMealsLiveData(): LiveData<List<CategoryMeal?>> {
        return categoryMealsLiveData
    }
}