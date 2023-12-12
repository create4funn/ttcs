package com.example.movie_ticket_booking.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Activity.AppData
import com.example.movie_ticket_booking.Activity.DetailActivity
import com.example.movie_ticket_booking.Adapter.FoodAdapter
import com.example.movie_ticket_booking.Adapter.MovieAdapter
import com.example.movie_ticket_booking.Model.FoodItem
import com.example.movie_ticket_booking.R

import com.google.firebase.firestore.FirebaseFirestore


class FoodFragment : Fragment() {

    private lateinit var db:FirebaseFirestore
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
        db = FirebaseFirestore.getInstance()
        db.collection("foods").addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Xử lý lỗi nếu có
                return@addSnapshotListener
            }
            val foodList = mutableListOf<FoodItem>()

            for (document in snapshot!!) {
                val id = document.id
                val name = document.getString("name")
                val img = document.getString("img")
                val des = document.getString("description")
                val price = document.getLong("price")


                val myData = FoodItem(id, name!!, des!!,  price!!,img!!)
                foodList.add(myData)

            }
            val foodAdapter = FoodAdapter()
            recyclerView.adapter = foodAdapter
            foodAdapter.setData(foodList)
        }



    }
}