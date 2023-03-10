package com.example.lastorderfood.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lastorderfood.activity.HomeActivity
import com.example.lastorderfood.activity.MealActivity
import com.example.lastorderfood.adapter.FavoriteAdapter
import com.example.lastorderfood.databinding.FragmentFavouritBinding
import com.example.lastorderfood.models.s.retrofit.Meal
import com.example.lastorderfood.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavouritFragment : Fragment() {
    private lateinit var binding: FragmentFavouritBinding
   private lateinit var favoriteMvvm:HomeViewModel
   private lateinit var favoriteAdapter:FavoriteAdapter
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteMvvm=(activity as HomeActivity).viewModel
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavouritBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareFavoriteAdapter()
        val itemTouchHelper=object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            )=true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position=viewHolder.adapterPosition
                val meal=favoriteAdapter.differ.currentList[position]
                favoriteMvvm.deleteMeal(meal)
                Snackbar.make(view,"Meal deleted",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo")
                {
                    favoriteMvvm.upinsertMeal(meal)

                }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoriteRecyclerView)
        observerFavorites()
        onMealClich()
    }
//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity).supportActionBar?.hide()
//    }

    private fun prepareFavoriteAdapter() {
        favoriteAdapter= FavoriteAdapter()
        binding.favoriteRecyclerView.apply {
            layoutManager= GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter=favoriteAdapter
        }
    }

    private fun observerFavorites() {

        favoriteMvvm.observeFavoriteMealLiveData().observe(viewLifecycleOwner,object :Observer<List<Meal?>> {

            override fun onChanged(t: List<Meal?>?) {
                favoriteAdapter.differ.submitList(t)

            }

        })
    }
    private fun onMealClich() {
        favoriteAdapter.onItemClick={
                meal-> val intent= Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

}