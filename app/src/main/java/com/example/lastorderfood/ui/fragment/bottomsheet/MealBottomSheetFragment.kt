package com.example.lastorderfood.ui.fragment.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.lastorderfood.activity.HomeActivity
import com.example.lastorderfood.activity.MealActivity
import com.example.lastorderfood.databinding.FragmentMealBottomSheetBinding
import com.example.lastorderfood.models.s.retrofit.Meal
import com.example.lastorderfood.ui.fragment.HomeFragment
import com.example.lastorderfood.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var bottomSheatMvvm:HomeViewModel
    private var mealId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
            bottomSheatMvvm=(activity as HomeActivity).viewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentMealBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheatMvvm.getMealDetails(mealId!!)
        observeBottomSheetMeal()
        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
       binding.bottomSheet.setOnClickListener {
           if(mealName!=null&&mealThumb!=null){
               val intent=Intent(activity,MealActivity::class.java)
               intent.apply {
                   putExtra(HomeFragment.MEAL_ID,mealId)
                   putExtra(HomeFragment.MEAL_NAME,mealName)
                   putExtra(HomeFragment.MEAL_THUMB,mealThumb)
               }
               startActivity(intent)
           }
       }
    }
     private var mealName:String?=null
    private var mealThumb:String?=null
    private fun observeBottomSheetMeal() {
        bottomSheatMvvm.observeMealDetailsLiveData().observe(viewLifecycleOwner,object : Observer<Meal?> {
            override fun onChanged(t: Meal?) {

                Glide.with(this@MealBottomSheetFragment)
                    .load(t!!.strMealThumb)
                    .into(binding.imgBottomSheet)

                binding.textCategory.text=t.strCategory
                binding.textPosition.text=t.strArea
                binding.mealName.text=t.strMeal
                binding.textCategory.text=t.strCategory
                binding.readMore.text="Read more...."
                mealName=t.strMeal
                mealThumb=t.strMealThumb
            }

        })
    }
}