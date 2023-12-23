package com.example.movie_ticket_booking.Adapter

import com.example.movie_ticket_booking.Model.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.Interface.FoodItemClickListener
import com.example.movie_ticket_booking.R
import com.squareup.picasso.Picasso


class FoodAdapter(private val context: SeatActivity, private val listener: FoodItemClickListener) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private var foodList: List<FoodItem> = listOf()
    private var selectedFood = mutableListOf<String>()
    private val quantityMap = mutableMapOf<Int, Int>()
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
        val increase = itemView.findViewById<ImageButton>(R.id.btnIncrease)
        val decrease = itemView.findViewById<ImageButton>(R.id.btnDecrease)
        val quantity = itemView.findViewById<TextView>(R.id.textViewQuantity)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        Picasso.get().load(currentItem.img).into(holder.image)
        holder.name.text = currentItem.name
        holder.des.text = currentItem.description
        holder.price.text = context.formatCurrency(currentItem.price)

        var currentQuantity = quantityMap[position] ?: 0

        // Sự kiện click cho nút Increase
        holder.increase.setOnClickListener {
            // Tăng số lượng khi nút Increase được nhấn
            currentQuantity += 1
            holder.quantity.text = currentQuantity.toString()

            val totalFoodPrice = currentItem.price * currentQuantity

            if (currentQuantity == 1) {
                selectedFood.add(currentItem.name)
                //gửi tên và tổng tiền item

//                listener.onFoodItemClicked(selectedFood, totalFoodPrice.toInt())

            }
                //cập nhật tổng tiền item
                listener.onFoodItemClicked(selectedFood, currentItem.price.toInt())

            quantityMap[position] = currentQuantity

        }

        // Sự kiện click cho nút Decrease
        holder.decrease.setOnClickListener {
            if (currentQuantity > 0) {
                currentQuantity -= 1
                holder.quantity.text = currentQuantity.toString()
                val totalFoodPrice = currentItem.price * currentQuantity

                if (currentQuantity == 0) {
                    selectedFood.remove(currentItem.name)
                }
                listener.onFoodItemClicked(selectedFood, -currentItem.price.toInt())
                quantityMap[position] = currentQuantity
            }
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}