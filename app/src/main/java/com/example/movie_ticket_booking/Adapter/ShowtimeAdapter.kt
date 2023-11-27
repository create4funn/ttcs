package com.example.movie_ticket_booking.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.R

class ShowtimeAdapter(private val onItemClick: (ShowtimeItem) -> Unit) : RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder>(){


    private var showtimeList: List<ShowtimeItem> = listOf()

    fun setData(showtime: List<ShowtimeItem>) {
        showtimeList = showtime
        notifyDataSetChanged()
    }
    inner class ShowtimeViewHolder(itemview: View) : ViewHolder(itemview) {
        val btnHour = itemview.findViewById<Button>(R.id.btn_hour)
//        init {
//            itemView.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val item = showtimeList[position]
//                    onItemClick(item)
//                }
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_showtime, parent,false)
        return ShowtimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showtimeList.size
    }

    override fun onBindViewHolder(holder: ShowtimeViewHolder, position: Int) {
        val currentItem = showtimeList[position]
        holder.btnHour.text = currentItem.showtime

        holder.itemView.setOnClickListener {
            onItemClick.invoke(currentItem)
        }
    }

}