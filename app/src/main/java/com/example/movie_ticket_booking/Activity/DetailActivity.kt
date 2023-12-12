package com.example.movie_ticket_booking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.movie_ticket_booking.R
import com.potyvideo.library.AndExoPlayerView
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var img : ImageView
    private lateinit var name : TextView
    private lateinit var duration : TextView
    private lateinit var age : TextView
    private lateinit var genre : TextView
    private lateinit var director : TextView
    private lateinit var cast : TextView
    private lateinit var summary : TextView
    private lateinit var video : AndExoPlayerView
    private lateinit var btnBack: ImageView
    private lateinit var btnBookDetail: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)

        val movie = AppData.selectedMovie

        initial()
        val check = intent.getBooleanExtra("check",false)
        if(!check){
            btnBookDetail.visibility = View.GONE
        }

        Picasso.get().load(movie?.img).into(img)
        name.text = movie?.name
        genre.text = movie?.genre
        director.text = movie?.director
        cast.text = movie?.cast
        duration.text = movie?.duration
        age.text = movie?.age
        summary.text = movie?.summary
        if (movie != null) {
            video.setSource(movie.trailer)
        }

        btnBookDetail.setOnClickListener {
            val intent = Intent(this, TheaterActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initial() {
        img = findViewById(R.id.img_detail)
        name = findViewById(R.id.detail_tv_movie_name)
        duration = findViewById(R.id.tv_duration)
        age = findViewById(R.id.tv_age)
        genre = findViewById(R.id.tv_genre)
        director = findViewById(R.id.tv_director)
        cast = findViewById(R.id.tv_cast)
        summary = findViewById(R.id.tv_summary)
        video = findViewById(R.id.videoview)
        btnBack = findViewById(R.id.btn_back_detail)
        btnBookDetail = findViewById<Button>(R.id.detail_btn_book_now)
    }


}