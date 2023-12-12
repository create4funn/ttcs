package com.example.movie_ticket_booking.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.movie_ticket_booking.R
import com.squareup.picasso.Picasso

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)

        val img = findViewById<ImageView>(R.id.payment_img)
        val name = findViewById<TextView>(R.id.payment_tv_tenphim)

        val movie = AppData.selectedMovie
        Picasso.get().load(movie?.img).into(img)
        name.text = movie?.name
    }




}