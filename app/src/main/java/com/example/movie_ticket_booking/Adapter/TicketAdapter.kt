package com.example.movie_ticket_booking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.Model.TicketItem
import com.example.movie_ticket_booking.R
import com.squareup.picasso.Picasso

class TicketAdapter(private val ticketList: List<TicketItem>, private val onItemClick: (TicketItem) -> Unit): RecyclerView.Adapter<TicketAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.ticket_img)
        val movieName = itemView.findViewById<TextView>(R.id.ticket_tv_tenphim)
        val theaterName = itemView.findViewById<TextView>(R.id.ticket_theater)
        val time = itemView.findViewById<TextView>(R.id.ticket_showtime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ticket_item, parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = ticketList[position]
        holder.movieName.text = currentItem.movie_name
        holder.theaterName.text = currentItem.theater_name
        holder.time.text = "${currentItem.hour}, ${currentItem.date}"
        Picasso.get().load(currentItem.imgMovie).into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(currentItem)
        }
    }
}