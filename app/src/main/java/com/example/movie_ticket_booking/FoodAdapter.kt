package com.example.recycleviewdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.R

data class FoodItem(
    val name: String,
    val description: String,
    val imgResourceId: Int,
    val price: Int
)


class FoodAdapter(private val foodItems: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodItems[position]
        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
        holder.imageView.setImageResource(currentItem.imgResourceId)
        holder.priceTextView.text = "${currentItem.price} VND"
    }

    override fun getItemCount(): Int {
        return foodItems.size
    }
}
