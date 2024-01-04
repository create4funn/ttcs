package com.example.movie_ticket_booking.Activity

import com.example.movie_ticket_booking.Model.*

object AppData {
    var selectedMovie: MovieItem? = null
    var selectedTheater: TheaterItem? = null
    var selectedShowtime: ShowtimeItem? = null
    var selectedTicket: TicketItem? = null
    var selectedSeats: List<SeatItem>? = null
    var selectedDate: String? = null
}
