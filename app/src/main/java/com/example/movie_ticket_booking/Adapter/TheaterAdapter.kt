package com.example.movie_ticket_booking.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Activity.AppData
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.Activity.TheaterActivity
import com.example.movie_ticket_booking.Model.Date
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.R

class TheaterAdapter(private val theatersList: List<TheaterItem>, private val date: String, private val check:Boolean): RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>() {




    class TheaterViewHolder(itemView: View): ViewHolder(itemView) {
        val nameTheater = itemView.findViewById<TextView>(R.id.tv_chose_theater)
        val rvShowtime = itemView.findViewById<RecyclerView>(R.id.rv_hour)
        val price = itemView.findViewById<TextView>(R.id.tv_ticket_price)
        val spacett = itemView.findViewById<TextView>(R.id.tv_distant)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheaterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_theater_item,parent,false)
        return TheaterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return theatersList.size
    }

    override fun onBindViewHolder(holder: TheaterViewHolder, position: Int) {
        val currentItem = theatersList[position]

        holder.nameTheater.text = theatersList[position].theaterName
        val movie = AppData.selectedMovie
        holder.price.text= movie?.price.toString()
        if (check){
            holder.spacett.visibility = View.VISIBLE
            holder.spacett.text = theatersList[position].spacetv.toString()+ " km"
        }
        holder.rvShowtime.apply {
            layoutManager = LinearLayoutManager(holder.rvShowtime.context, RecyclerView.HORIZONTAL, false)
            adapter = ShowtimeAdapter(currentItem.showtimeList){showtimeItem ->
                AppData.selectedShowtime = showtimeItem
                AppData.selectedTheater = currentItem
                AppData.selectedDate = date

                Log.d("abcc","${AppData.selectedDate} ${AppData.selectedShowtime} ${AppData.selectedTheater}")
                val intent = Intent(context, SeatActivity::class.java)
                startActivity(context,intent,null)
            }
        }

    }
}



