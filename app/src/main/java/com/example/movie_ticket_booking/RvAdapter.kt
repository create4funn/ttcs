package com.example.movie_ticket_booking


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(private val filmdangchieu: List<listfilm>) : RecyclerView.Adapter<RvAdapter.PhimViewHolder>() {

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
        val currentItem = filmdangchieu[position]
        holder.imageView.setImageResource(currentItem.image)
        holder.tenfilmTextView.text = currentItem.tenfilm
        holder.danhgiaTextView.text = currentItem.danhgia
    }

    override fun getItemCount(): Int {
        return filmdangchieu.size
    }
}