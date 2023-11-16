package com.example.movie_ticket_booking.Fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.R


class SeatFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seatLayout = view.findViewById<GridLayout>(R.id.gv_seatLayout)

        val numRows = 9
        val seatsPerRow = 8
        var listRow: Char = 'A'
        for (row in 1..numRows) {
            for (seatNumber in 1..seatsPerRow) {
                val seat = Button(requireContext())
                seat.text = "$listRow$seatNumber"
                seat.textSize = 14f
                seat.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)

                }
                seat.gravity = Gravity.CENTER

                seatLayout.addView(seat)
            }
            listRow += 1
        }


    }

}