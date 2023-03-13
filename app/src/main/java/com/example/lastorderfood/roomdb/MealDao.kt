package com.example.lastorderfood.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.lastorderfood.models.s.retrofit.Meal


@Dao
interface MealDao {



    @Insert(onConflict = REPLACE)
    suspend fun upsert(meal: Meal)
    @Delete
    suspend fun delete(meal: Meal)
    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>

}