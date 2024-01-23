package com.example.movie_ticket_booking.Model

import com.google.firebase.firestore.GeoPoint

data class TheaterItem(
    val id: String,
    val theaterName: String,
    val address: String,
    val showtimeList: List<ShowtimeItem>,
    val mapth: GeoPoint,
    val spacetv: Double

)
