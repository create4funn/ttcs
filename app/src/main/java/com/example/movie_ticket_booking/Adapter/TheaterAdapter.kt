package com.example.movie_ticket_booking.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Activity.AppData
import com.example.movie_ticket_booking.Activity.DetailActivity
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.Activity.TheaterActivity
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.R

class TheaterAdapter(private val context: Context): RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>() {

    private var theatersList: List<TheaterItem> = listOf()
    fun setData(theaters: List<TheaterItem>) {
        theatersList = theaters
        notifyDataSetChanged()
    }
    class TheaterViewHolder(itemView: View): ViewHolder(itemView) {
        val nameTheater = itemView.findViewById<TextView>(R.id.tv_chose_theater)
        val rvShowtime = itemView.findViewById<RecyclerView>(R.id.rv_hour)
        val price = itemView.findViewById<TextView>(R.id.tv_ticket_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheaterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_theater_item,parent,false)
        return TheaterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return theatersList.size
    }

    override fun onBindViewHolder(holder: TheaterViewHolder, position: Int) {
        holder.nameTheater.text = theatersList[position].theaterName
        val movie = AppData.selectedMovie
        holder.price.text= movie?.price.toString()

        val showtimeAdapter = ShowtimeAdapter{ selectedShowtime ->
            AppData.selectedShowtime = selectedShowtime
            val intent = Intent(context, SeatActivity::class.java)
            context.startActivity(intent)
        }
        holder.rvShowtime.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = showtimeAdapter
            showtimeAdapter.setData(theatersList[position].showtimeList)
        }

    }
}