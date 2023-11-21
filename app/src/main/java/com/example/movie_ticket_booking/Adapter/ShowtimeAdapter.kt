package com.example.movie_ticket_booking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.R

class ShowtimeAdapter(private val showtimeList: List<ShowtimeItem>) : RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder>(){

    class ShowtimeViewHolder(itemview: View) : ViewHolder(itemview) {
        val btnHour = itemview.findViewById<Button>(R.id.btn_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_showtime, parent,false)
        return ShowtimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    override fun onBindViewHolder(holder: ShowtimeViewHolder, position: Int) {
        holder.btnHour.text = showtimeList[position].showtime
    }

}