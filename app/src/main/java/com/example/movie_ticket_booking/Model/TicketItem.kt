package com.example.movie_ticket_booking.Model

data class TicketItem(
    val ticketID: String,
    val imgMovie:String,
//    val imgQr: String,
    val movie_name: String,
    val theater_name: String,
    val date: String,
    val hour: String,
    val seat: String,
    val food: String?,
    val total: Long
)
