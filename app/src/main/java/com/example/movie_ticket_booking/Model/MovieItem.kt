package com.example.movie_ticket_booking.Model

import java.sql.Timestamp


data class MovieItem (
    val id: String ="",
    val img: String="",
    val name:String="",
    val duration:String="",
    val age:String="",
    val cast:String="",
    val director:String="",
    val price:Long =0,
    val summary:String="",
    val trailer:String="",
//    val start:com.google.firebase.Timestamp,
//    val end:com.google.firebase.Timestamp,
    val genre: String=""


)

