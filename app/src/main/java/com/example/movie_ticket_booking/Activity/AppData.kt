package com.example.movie_ticket_booking.Activity

import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.Model.MovieItem
import com.example.movie_ticket_booking.Model.SeatItem

object AppData {
    var selectedMovie: MovieItem? = null
    var selectedTheater: TheaterItem? = null
    var selectedShowtime: ShowtimeItem? = null
    var selectedSeats: List<SeatItem>? = null

}
