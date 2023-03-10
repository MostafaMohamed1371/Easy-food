package com.example.lastorderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastorderfood.databinding.CategoryItemsBinding
import com.example.lastorderfood.models.s.retrofit.Category
import com.example.lastorderfood.models.s.retrofit.CategoryMeal
import com.example.lastorderfood.models.s.retrofit.Meal


class CategoryAdapter(): RecyclerView.Adapter<CategoryAdapter.MyviewHolder>(){
    private var categoriesList=ArrayList<Category>()
    lateinit var onItemClick:((Category)->Unit)
    fun setCategories(categoriesList:ArrayList<Category>){
        this.categoriesList=categoriesList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        return MyviewHolder(CategoryItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
      Glide.with(holder.itemView)
          .load(categoriesList[position].strCategoryThumb)
          .into(holder.binding.imageCategory)
        holder.binding.textCategory.text=categoriesList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoriesList[position])
        }


    }


    override fun getItemCount(): Int {
        return categoriesList.size
    }

    class  MyviewHolder(var binding: CategoryItemsBinding) : RecyclerView.ViewHolder(binding.root)

}




