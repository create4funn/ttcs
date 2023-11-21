package com.example.movie_ticket_booking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.R

class TheaterAdapter(private val theaterList: List<TheaterItem>): RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>() {
    class TheaterViewHolder(itemView: View): ViewHolder(itemView) {
        val nameTheater = itemView.findViewById<TextView>(R.id.tv_chose_theater)
        val rvShowtime = itemView.findViewById<RecyclerView>(R.id.rv_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheaterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_theater_item,parent,false)
        return TheaterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return theaterList.size
    }

    override fun onBindViewHolder(holder: TheaterViewHolder, position: Int) {
        holder.nameTheater.text = theaterList[position].theaterName
        val showtimeAdapter = ShowtimeAdapter(theaterList[position].showtimeList)
        holder.rvShowtime.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showtimeAdapter
        }
    }
}