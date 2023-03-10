package com.example.lastorderfood.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lastorderfood.activity.HomeActivity
import com.example.lastorderfood.activity.MealActivity
import com.example.lastorderfood.adapter.FavoriteAdapter
import com.example.lastorderfood.databinding.FragmentSearchBinding
import com.example.lastorderfood.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchMvvm:HomeViewModel
    private lateinit var searchAdapter:FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchMvvm=(activity as HomeActivity).viewModel
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrepareAdapter()
        binding.arrowSearch.setOnClickListener {
            searchMeals()
        }

        observeSearchLiveData()

        var searchJob:Job?=null
        binding.boxSearch.addTextChangedListener{search->
            searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(250)
                searchMvvm.getSearchMeals(search.toString())
            }
        }
        onMealClich()

    }

//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity).supportActionBar?.hide()
//    }

    private fun observeSearchLiveData() {
        searchMvvm.observeSearchMealsLiveData().observe(viewLifecycleOwner, Observer{
            mealList->searchAdapter.differ.submitList(mealList)
        })
    }

    private fun searchMeals() {
        val search=binding.boxSearch.text.toString()
        if (search.isNotEmpty()){
            searchMvvm.getSearchMeals(search)
        }
    }

    private fun PrepareAdapter() {
        searchAdapter=FavoriteAdapter()
        binding.searchRecyclerView.apply {
            layoutManager= GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter=searchAdapter
        }
    }
    private fun onMealClich() {
        searchAdapter.onItemClick={
                meal-> val intent= Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }
}