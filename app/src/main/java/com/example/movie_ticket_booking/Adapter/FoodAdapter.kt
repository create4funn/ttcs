package com.example.movie_ticket_booking.Adapter

import com.example.movie_ticket_booking.Model.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.R
import com.squareup.picasso.Picasso

class FoodAdapter() : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private var foodList: List<FoodItem> = listOf()

    fun setData(foods: List<FoodItem>) {
        foodList = foods
        notifyDataSetChanged()
    }
    // ViewHolder class
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.textViewName)
        val des = itemView.findViewById<TextView>(R.id.textViewDescription)
        val price = itemView.findViewById<TextView>(R.id.textViewPrice)
        val image = itemView.findViewById<ImageView>(R.id.img_food)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        Picasso.get().load(currentItem.img).into(holder.image)
        holder.name.text = currentItem.name
        holder.des.text = currentItem.description
        holder.price.text = currentItem.price.toString()


//        holder.itemView.setOnClickListener {
//            onItemClick.invoke(currentItem)
//        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}