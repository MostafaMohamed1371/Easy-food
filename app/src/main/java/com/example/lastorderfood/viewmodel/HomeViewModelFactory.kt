package com.example.lastorderfood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lastorderfood.roomdb.MealDatabase

class HomeViewModelFactory(
  private val mealDatabase: MealDatabase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}