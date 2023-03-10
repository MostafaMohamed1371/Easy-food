package com.example.lastorderfood.viewmodel

import android.icu.text.StringSearch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lastorderfood.models.s.retrofit.*
import com.example.lastorderfood.roomdb.MealDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData=MutableLiveData<Meal?>()
    private var popularItemsLiveData= MutableLiveData<List<CategoryMeal?>>()
    private var categoriesItemsLiveData= MutableLiveData<List<Category?>>()
    private  var favoriteMealLiveData=mealDatabase.getMealDao().getAllMeals()
    private var MealDetailsLiveData=MutableLiveData<Meal?>()
    private var searchMealsLiveData=MutableLiveData<List<Meal>?>()
    fun getRandomMeal() {
        MealInstance.api.getRandamMeal().enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal? = response.body()!!.meals?.get(0)
                    randomMealLiveData.value=randomMeal

                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }
    fun getPopularItems(){
        MealInstance.api.getPopularItem("Seafood").enqueue(object :retrofit2.Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    popularItemsLiveData.value= response.body()!!.meals!!
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
              Log.d("HomeFragment",t.message.toString())
            }

        })


    }
    fun getCategoriesItems(){
        MealInstance.api.getCategories().enqueue(object :retrofit2.Callback<CategoryItems>{

            override fun onResponse(call: Call<CategoryItems>, response: Response<CategoryItems>) {
                if (response.body() != null) {
                    categoriesItemsLiveData.value=response.body()!!.categories!!
                }
            }

            override fun onFailure(call: Call<CategoryItems>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }


        })


    }

    fun getMealDetails(id:String) {
        MealInstance.api.getMealDetails(id).enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val mealDetails: Meal? = response.body()!!.meals?.first()
                    MealDetailsLiveData.value=mealDetails

                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getSearchMeals(search:String) {
        MealInstance.api.getSearchMeals(search).enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val search = response.body()?.meals
                    search?.let {
                        searchMealsLiveData.postValue(it as List<Meal>)
                    }

                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }




    fun observeRandomMealLiveData(): LiveData<Meal?> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<CategoryMeal?>> {
        return popularItemsLiveData
    }

    fun observeCategoriesItemsLiveData(): LiveData<List<Category?>> {
        return categoriesItemsLiveData
    }
    fun observeFavoriteMealLiveData(): LiveData<List<Meal>> {
        return favoriteMealLiveData
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


    fun observeMealDetailsLiveData(): LiveData<Meal?> {
        return MealDetailsLiveData
    }

    fun observeSearchMealsLiveData(): LiveData<List<Meal>?> {
        return searchMealsLiveData
    }
}