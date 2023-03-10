package com.example.lastorderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastorderfood.databinding.CategoryItemsBinding
import com.example.lastorderfood.databinding.MealsItemBinding
import com.example.lastorderfood.models.s.retrofit.Category
import com.example.lastorderfood.models.s.retrofit.CategoryMeal

class MealAdapter (): RecyclerView.Adapter<MealAdapter.MyviewHolder>(){
    private var mealsList=ArrayList<CategoryMeal>()
    lateinit var onItemClick:((CategoryMeal)->Unit)
    fun setMeals(mealsList:ArrayList<CategoryMeal>){
        this.mealsList=mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        return MyviewHolder(MealsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imageMeal)
        holder.binding.textMeal.text=mealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }

    }


    override fun getItemCount(): Int {
        return mealsList.size
    }

    class  MyviewHolder(var binding: MealsItemBinding) : RecyclerView.ViewHolder(binding.root)

}
