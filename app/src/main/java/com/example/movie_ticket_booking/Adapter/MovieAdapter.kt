package com.example.movie_ticket_booking.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.R
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext

class MovieAdapter(private val onItemClick: (MovieItem) -> Unit) : RecyclerView.Adapter<MovieAdapter.PhimViewHolder>() {

    private var moviesList: List<MovieItem> = listOf()

    fun setData(movies: List<MovieItem>) {
        moviesList = movies
        notifyDataSetChanged()
    }
    // ViewHolder class
    class PhimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgfilm)
        val tenfilmTextView: TextView = itemView.findViewById(R.id.txttenfilm)
        val danhgiaTextView: TextView = itemView.findViewById(R.id.txtdanhgia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhimViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rvfilm, parent, false)
        return PhimViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhimViewHolder, position: Int) {
        val currentItem = moviesList[position]
        Picasso.get().load(currentItem.img).into(holder.imageView)
        holder.tenfilmTextView.text = currentItem.name
        holder.danhgiaTextView.text = currentItem.duration

        holder.itemView.setOnClickListener {
            onItemClick.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}