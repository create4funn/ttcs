package com.example.movie_ticket_booking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.R



class ShowtimeAdapter(private val showtimeList: List<ShowtimeItem>, private val onItemClick: (ShowtimeItem) -> Unit) : RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder>(){


    inner class ShowtimeViewHolder(itemview: View) : ViewHolder(itemview) {
        val btnHour = itemview.findViewById<TextView>(R.id.btn_hour)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_showtime, parent,false)
        return ShowtimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    override fun onBindViewHolder(holder: ShowtimeViewHolder, position: Int) {
        val item = showtimeList[position]
        holder.btnHour.text = item.showtime

        holder.itemView.setOnClickListener {
            onItemClick.invoke(item)
        }
    }

}