package com.example.movie_ticket_booking.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.R
import com.example.recycleviewdemo.FoodAdapter
import com.example.recycleviewdemo.FoodItem


class FoodFragment : Fragment() {
    private val foodItems = ArrayList<FoodItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_food)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoodAdapter(foodItems)
        recyclerView.adapter = adapter

        // Add sample food items
        foodItems.add(
            FoodItem("Pizza", "Delicious pizza with your choice of toppings.",
            R.drawable.pizza,20000)
        )
        foodItems.add(
            FoodItem("Burger", "Juicy burgers with a variety of ingredients.",
            R.drawable.hamburger,16000)
        )
        foodItems.add(
            FoodItem("Popcorn","You can choose some seasonings to mix with it.",
            R.drawable.popcorn,25000)
        )
        // Add more food items as needed
        foodItems.add(FoodItem("French Fried","", R.drawable.french_fried,100000))
        foodItems.add(FoodItem("Milo","", R.drawable.milo_lon,30000))
        foodItems.add(FoodItem("Coca Cola","", R.drawable.coca_cola,15000))
        foodItems.add(FoodItem("Pepsi","", R.drawable.pepsi,15000))
        foodItems.add(
            FoodItem("Combo 1","Include: 1 beef burger size L, and Any type of drink.",
            R.drawable.combo_1bur_1frfried,100000)
        )
        foodItems.add(FoodItem("Black Coffee","", R.drawable.coffee,20000))
        foodItems.add(FoodItem("Snack","", R.drawable.snack,10000))
        adapter.notifyDataSetChanged()


    }
}