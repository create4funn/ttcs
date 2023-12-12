package com.example.movie_ticket_booking.Model

sealed class ListItem{
    data class TheaterItem(
        val id: String,
        val theaterName: String,
        val address: String,
        val showtimeList: List<ShowtimeItem>
    )

}
