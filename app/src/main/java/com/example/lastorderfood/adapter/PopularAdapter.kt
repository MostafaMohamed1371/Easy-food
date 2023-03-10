package com.example.lastorderfood.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lastorderfood.databinding.PopularItemsBinding
import com.example.lastorderfood.models.s.retrofit.CategoryMeal


class PopularAdapter (): RecyclerView.Adapter<PopularAdapter.MyviewHolder>(){
  lateinit var onItemClick:((CategoryMeal)->Unit)
     var onLongItemClick:((CategoryMeal)->Unit)?=null
 private var mealsList=ArrayList<CategoryMeal>()
    fun setMeals(mealsList:ArrayList<CategoryMeal>){
        this.mealsList=mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
    return MyviewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(mealsList[position].strMealThumb)
           .into(holder.binding.imgPopularItems)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnClickListener {
            onLongItemClick!!.invoke(mealsList[position])
        }

    }


    override fun getItemCount(): Int {
     return mealsList.size
    }

    class  MyviewHolder(var binding: PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)

}





