package com.example.lastorderfood.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lastorderfood.activity.CategoryMealsActivity
import com.example.lastorderfood.activity.HomeActivity
import com.example.lastorderfood.adapter.CategoryAdapter
import com.example.lastorderfood.adapter.MealAdapter
import com.example.lastorderfood.databinding.FragmentCategoriesBinding
import com.example.lastorderfood.models.s.retrofit.Category
import com.example.lastorderfood.viewmodel.HomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoriesFragment : Fragment() {
  private lateinit var binding: FragmentCategoriesBinding
  private lateinit var categoriesMvvm:HomeViewModel
  private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var MealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriesMvvm=(activity as HomeActivity).viewModel
        MealAdapter=MealAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoryItemsRecyclerView()
        categoriesMvvm.getCategoriesItems()
        observerCategoryItems()
        onClickCategory()

    }

//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity).supportActionBar?.hide()
//    }
    private fun onClickCategory() {
        categoryAdapter.onItemClick={
                category-> val intent=Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }


    private fun prepareCategoryItemsRecyclerView() {
        categoryAdapter= CategoryAdapter()
        binding.categoriesRecyclerView.apply {
            layoutManager= GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter=categoryAdapter
        }
    }
    private fun observerCategoryItems() {
        categoriesMvvm.observeCategoriesItemsLiveData().observe(viewLifecycleOwner,object :
            Observer<List<Category?>> {

            override fun onChanged(t: List<Category?>?) {
                categoryAdapter.setCategories(t as ArrayList<Category>)

            }

        })
    }

}