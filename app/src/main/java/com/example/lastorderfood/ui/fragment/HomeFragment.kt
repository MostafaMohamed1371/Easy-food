package com.example.lastorderfood.ui.fragment

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lastorderfood.activity.*
import com.example.lastorderfood.adapter.CategoryAdapter
import com.example.lastorderfood.adapter.PopularAdapter

import com.example.lastorderfood.databinding.FragmentHomeBinding
import com.example.lastorderfood.models.s.retrofit.Category
import com.example.lastorderfood.models.s.retrofit.CategoryMeal
import com.example.lastorderfood.models.s.retrofit.Meal
import com.example.lastorderfood.ui.fragment.bottomsheet.MealBottomSheetFragment

import com.example.lastorderfood.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
   private lateinit var homeMvvm:HomeViewModel
   private lateinit var randamMeal:Meal
   private lateinit var popularAdapter: PopularAdapter
   private lateinit var categoryAdapter: CategoryAdapter
   private lateinit var user:FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userId: String
 //  private lateinit var name:String
   companion object{
       const val MEAL_ID="com.example.lastorderfood.ui.fragment.idMeal"
       const val MEAL_NAME="com.example.lastorderfood.ui.fragment.nameMeal"
       const val MEAL_THUMB="com.example.lastorderfood.ui.fragment.thumbMeal"
       const val CATEGORY_NAME="com.example.lastorderfood.ui.fragment.categoryName"
   }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      var bundle=arguments
//       if (bundle!=null){
//         name= bundle.getString(RegisterActivity.USERNAME).toString()
//        }


        homeMvvm=(activity as HomeActivity).viewModel
        popularAdapter=PopularAdapter()
        categoryAdapter= CategoryAdapter()
        user= FirebaseAuth.getInstance().currentUser!!
        reference=FirebaseDatabase.getInstance().getReference("Register")
        userId=user.uid
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemsRecyclerView()
        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()
        homeMvvm.getPopularItems()
        observerPopularItems()
        onPopulatItemClich()
        prepareCategoryItemsRecyclerView()
        homeMvvm.getCategoriesItems()
        observerCategoryItems()
        onCategoryItemClich()
        onLongItemClich()
       // onSearchIconClick()
       // onLogoutIconClick()

//        binding.textUser.text=RegisterActivity.Register.user
      //  readDb()
       // referenceRealDb()

    }

//    private fun referenceRealDb() {
//        reference.child(userId).addListenerForSingleValueEvent(object :ValueEventListener{
//
////            override fun onDataChange(snapshot: DataSnapshot) {
////               var userProfile: DataLogin= snapshot.getValue(DataLogin::class.java)!!
////                if (userProfile!=null){
////                    binding.textUser.text=userProfile.userName
////                }
////
////            }
//
//
//            override fun onCancelled(error: DatabaseError) {
//                StyleableToast.makeText(activity!!,"sucess add",R.style.exampleToast).show()
//            }
//
//        })
//    }

//    private fun readDb() {
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Register")
//        myRef.addValueEventListener(object :ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//              var name:String=snapshot.child("abo").child("userName").getValue(String::class.java)!!
//                binding.textUser.text=name
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//               var error:String=error.message
//                Toast.makeText(activity,error,Toast.LENGTH_LONG).show()
//            }
//
//        })
//    }

//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity).supportActionBar?.show()
//    }

//    private fun onLogoutIconClick() {
//        binding.logout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            val intent=Intent(activity,LoginActivity::class.java)
//            startActivity(intent)
//            activity?.finish()
//
//
//
//        }
//    }

//    private fun onSearchIconClick() {
//        binding.searchImage.setOnClickListener {
//        findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
//
//        }
//    }

    private fun onLongItemClich() {
        popularAdapter.onLongItemClick={
                meal->
            val mealBottomSheetFragment= meal.idMeal?.let { MealBottomSheetFragment.newInstance(it) }

            mealBottomSheetFragment!!.show(childFragmentManager,"Meal Info")

        }
    }

    private fun onCategoryItemClich() {
        categoryAdapter.onItemClick={
                category-> val intent=Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoryItemsRecyclerView() {
        categoryAdapter= CategoryAdapter()
       binding.recyclerViewCategory.apply {
           layoutManager=GridLayoutManager(context,4,GridLayoutManager.VERTICAL,false)
           adapter=categoryAdapter
       }
    }

    private fun observerCategoryItems() {
        homeMvvm.observeCategoriesItemsLiveData().observe(viewLifecycleOwner,object :Observer<List<Category?>> {

            override fun onChanged(t: List<Category?>?) {
                categoryAdapter.setCategories(t as ArrayList<Category>)
            }

        })
    }

    private fun onPopulatItemClich() {
        popularAdapter.onItemClick={
            meal-> val intent=Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerView.apply {
         layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter=popularAdapter
        }
    }

    private fun observerPopularItems() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner,object :Observer<List<CategoryMeal?>>{

            override fun onChanged(t: List<CategoryMeal?>?) {
                popularAdapter.setMeals(t as ArrayList<CategoryMeal>)
            }


        })
    }

    private fun onRandomMealClick() {
       binding.randomCardView.setOnClickListener {
           val intent=Intent(activity,MealActivity::class.java)
           intent.putExtra(MEAL_ID,randamMeal.idMeal)
           intent.putExtra(MEAL_NAME,randamMeal.strMeal)
           intent.putExtra(MEAL_THUMB,randamMeal.strMealThumb)
           startActivity(intent)
       }
    }

    private fun observerRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner,object :Observer<Meal?>{
            override fun onChanged(t: Meal?) {

               Glide.with(this@HomeFragment)
                   .load(t!!.strMealThumb)
                   .into(binding.randomImage)
               this@HomeFragment.randamMeal=t

            }

        })
    }


}






