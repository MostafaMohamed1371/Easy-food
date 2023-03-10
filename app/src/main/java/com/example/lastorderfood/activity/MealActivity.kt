package com.example.lastorderfood.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.ActivityMealBinding
import com.example.lastorderfood.models.s.retrofit.Meal
import com.example.lastorderfood.roomdb.MealDatabase
import com.example.lastorderfood.ui.fragment.HomeFragment
import com.example.lastorderfood.viewmodel.HomeViewModel
import com.example.lastorderfood.viewmodel.MealViewModel
import com.example.lastorderfood.viewmodel.MealViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import io.github.muddz.styleabletoast.StyleableToast

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase=MealDatabase.getinstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this@MealActivity,viewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setInformationInView()
        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observerMealDetails()
        onYoutubeImageClick()
        onFavoriteClick()


    }

    private fun onFavoriteClick() {
        binding.btnAddFav.setOnClickListener {
            mealSave.let {meal->
                if (meal != null) {
                    mealMvvm.upinsertMeal(meal)
                    StyleableToast.makeText(this,"Meal Saved", R.style.exampleToast).show()
                    //Toast.makeText(this,"Meal Saved",Toast.LENGTH_SHORT).show()

                }
            }

            }

        }


    private fun onYoutubeImageClick() {
        binding.youtube.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

   private var mealSave:Meal?=null
    private fun observerMealDetails() {
        mealMvvm.observeMealDetailLiveData().observe(this,object : Observer<Meal?> {
            override fun onChanged(t: Meal?) {
                onResponceCase()
                mealSave=t
            binding.textCategory.text="Category:${t!!.strCategory}"
            binding.textArea.text="Area:${t!!.strArea}"
            binding.instruction.text=t!!.strInstructions
                youtubeLink= t.strYoutube.toString()

            }

        })
    }

    private fun setInformationInView() {
        Glide.with(this)
            .load(mealThumb)
            .into(binding.imagebar)
        binding.collapsingToolbar.title=mealName
    }

    private fun getMealInformationFromIntent() {
        val intent=intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.textCategory.visibility=View.INVISIBLE
        binding.textArea.visibility=View.INVISIBLE
        binding.globalInstruction.visibility=View.INVISIBLE
        binding.btnAddFav.visibility=View.INVISIBLE
        binding.youtube.visibility=View.INVISIBLE

    }
    private fun onResponceCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.textCategory.visibility=View.VISIBLE
        binding.textArea.visibility=View.VISIBLE
        binding.globalInstruction.visibility=View.VISIBLE
        binding.btnAddFav.visibility=View.VISIBLE
        binding.youtube.visibility=View.VISIBLE

    }
}


