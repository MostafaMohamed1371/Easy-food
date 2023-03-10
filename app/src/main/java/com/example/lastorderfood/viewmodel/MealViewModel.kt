package com.example.lastorderfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastorderfood.models.s.retrofit.Meal
import com.example.lastorderfood.models.s.retrofit.MealInstance
import com.example.lastorderfood.models.s.retrofit.MealList
import com.example.lastorderfood.roomdb.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {
    private var mealDetailLiveData= MutableLiveData<Meal?>()
    fun getMealDetails(id:String) {
        MealInstance.api.getMealDetails(id).enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val mealDetails: Meal? = response.body()!!.meals?.get(0)
                    mealDetailLiveData.value=mealDetails

                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }

        })
    }
    fun observeMealDetailLiveData(): LiveData<Meal?> {
        return mealDetailLiveData
    }
    fun upinsertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.getMealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.getMealDao().delete(meal)
        }
    }

}