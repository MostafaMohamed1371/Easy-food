package com.example.lastorderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastorderfood.databinding.MealsItemBinding
import com.example.lastorderfood.models.s.retrofit.CategoryMeal
import com.example.lastorderfood.models.s.retrofit.Meal
import com.google.firebase.auth.FirebaseAuth

class FavoriteAdapter (): RecyclerView.Adapter<FavoriteAdapter.MyviewHolder>(){

    lateinit var onItemClick:((Meal)->Unit)
    private val diffUtil=object :DiffUtil.ItemCallback<Meal>(){

        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {

              return oldItem.idMeal==newItem.idMeal

        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
          return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        return MyviewHolder(MealsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(differ.currentList[position].strMealThumb)
            .into(holder.binding.imageMeal)
        holder.binding.textMeal.text=differ.currentList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(differ.currentList[position])
        }

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class  MyviewHolder(var binding: MealsItemBinding) : RecyclerView.ViewHolder(binding.root)

}


