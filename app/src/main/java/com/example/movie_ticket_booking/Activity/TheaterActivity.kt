package com.example.movie_ticket_booking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.movie_ticket_booking.R
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.util.*

class TheaterActivity : AppCompatActivity() {
    private lateinit var calendarTextView: TextView
    private var year = 0
    private var month = 0
    private var day = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choosetheater)

        calendarTextView = findViewById(R.id.cal_tv)
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)


        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        // Horizontal Calendar setup
        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendar_view)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .build()


        year = startDate.get(Calendar.YEAR)
        month = startDate.get(Calendar.MONTH) + 1
        day = startDate.get(Calendar.DAY_OF_MONTH)
        updateCalendarTextView()


        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                year = date.get(Calendar.YEAR)
                month = date.get(Calendar.MONTH) + 1
                day = date.get(Calendar.DAY_OF_MONTH)
                updateCalendarTextView()
            }
        }

        val btnNext = findViewById<Button>(R.id.btn_next)
        btnNext.setOnClickListener {
            val intent = Intent(this, SeatActivity::class.java)
            startActivity(intent)
        }
    }
    private fun updateCalendarTextView() {
        calendarTextView.text = "$day/$month/$year "
    }


}