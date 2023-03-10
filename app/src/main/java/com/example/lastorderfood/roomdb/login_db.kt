package com.example.lastorderfood.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "userlogin")
data class login_db(
    @PrimaryKey
    val login_db:String,
    val idMeal: String

)
