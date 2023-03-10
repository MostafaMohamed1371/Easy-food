package com.example.lastorderfood.roomdb

import androidx.room.Embedded
import androidx.room.Relation
import com.example.lastorderfood.models.s.retrofit.Meal

data class loginANdMeal(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "idMeal",
        entityColumn = "idMeal"
    )
    val login_db:login_db
)
