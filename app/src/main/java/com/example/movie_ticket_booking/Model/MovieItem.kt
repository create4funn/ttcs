package com.example.movie_ticket_booking.Model

import java.sql.Timestamp


data class MovieItem (
    val id: String,
    val img: String,
    val name:String,
    val duration:String,
    val age:String,
    val cast:String,
    val director:String,
    val price:Long,
    val summary:String,
    val trailer:String,
    val genre: String


)

