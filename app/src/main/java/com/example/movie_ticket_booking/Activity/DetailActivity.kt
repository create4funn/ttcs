package com.example.movie_ticket_booking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.movie_ticket_booking.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)

        val btnBookDetail = findViewById<Button>(R.id.detail_btn_book_now)
        btnBookDetail.setOnClickListener {
            val intent = Intent(this, TheaterActivity::class.java)
            startActivity(intent)
        }
    }
}