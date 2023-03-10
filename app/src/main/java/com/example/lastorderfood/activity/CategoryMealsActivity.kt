package com.example.lastorderfood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lastorderfood.R
import com.example.lastorderfood.adapter.CategoryAdapter
import com.example.lastorderfood.adapter.MealAdapter
import com.example.lastorderfood.databinding.ActivityCategoryMealsBinding
import com.example.lastorderfood.models.s.retrofit.Category
import com.example.lastorderfood.models.s.retrofit.CategoryMeal
import com.example.lastorderfood.ui.fragment.HomeFragment
import com.example.lastorderfood.viewmodel.CategoryMealsViewModel


class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMvvm:CategoryMealsViewModel
    private lateinit var mealsAdapter: MealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryMvvm= ViewModelProvider(this@CategoryMealsActivity)[CategoryMealsViewModel::class.java]
        prepareMealsRecyclerView()
        categoryMvvm.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        observerMealsItems()
        onMealClich()

    }

    private fun observerMealsItems() {
        categoryMvvm.observeCategoryMealsLiveData().observe(this,object :
            Observer<List<CategoryMeal?>> {

            override fun onChanged(t: List<CategoryMeal?>?) {
                mealsAdapter.setMeals(t as ArrayList<CategoryMeal>)
                binding.textCount.text=t.size.toString()
            }

        })
    }

    private fun prepareMealsRecyclerView() {
        mealsAdapter= MealAdapter()
        binding.recyclerViewMeals.apply {
            layoutManager= GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter=mealsAdapter
        }
    }
    private fun onMealClich() {
        mealsAdapter.onItemClick={
                meal-> val intent= Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }
}