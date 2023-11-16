package com.example.movie_ticket_booking.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movie_ticket_booking.Fragment.FoodFragment
import com.example.movie_ticket_booking.Fragment.SeatFragment
import com.example.movie_ticket_booking.R

class SeatActivity : AppCompatActivity() {
    private lateinit var seatLayout: GridLayout
    private lateinit var reserveButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movieseat)
        //seatLayout = findViewById(R.id.gv_seatLayout)

        //initializeSeatLayout()

//        reserveButton.setOnClickListener {
//            // Handle seat reservation logic here
//            val reservedSeats = getReservedSeats()
//            // Display a message with the reserved seats
//            Toast.makeText(this, "You choose: $reservedSeats", Toast.LENGTH_SHORT).show()
//        }

//        val btn_seat_book = findViewById<Button>(R.id.btn_seat_book)
//        btn_seat_book.setOnClickListener {
//            val intent = Intent(this, FoodActivity::class.java)
//            startActivity(intent)
//        }

        replaceFragment(SeatFragment())
        val btnbook = findViewById<Button>(R.id.btn_seat_book)
        btnbook.setOnClickListener {

            replaceFragment(FoodFragment())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_seat, fragment).commit()
    }










//    private fun initializeSeatLayout() {
//        val numRows = 9
//        val seatsPerRow = 8
//        var listRow: Char = 'A'
//        for (row in 1..numRows) {
//            for (seatNumber in 1..seatsPerRow) {
//                val seat = Button(this)
//                seat.text = "$listRow$seatNumber"
//                seat.textSize = 14f
//                seat.layoutParams = GridLayout.LayoutParams().apply {
//                    width = 0
//                    height = GridLayout.LayoutParams.WRAP_CONTENT
//                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
//
//                }
//                seat.gravity = Gravity.CENTER
//
//                seatLayout.addView(seat)
//            }
//            listRow += 1
//        }
//    }
//
//
//    private fun getReservedSeats(): List<String> {
//        // Implement logic to get the list of reserved seats
//        // For example, you can iterate through the seatLayout and check which seats are reserved.
//        val reservedSeats = mutableListOf<String>()
//        for (row in 0 until seatLayout.rowCount) {
//            for (col in 0 until seatLayout.columnCount) {
//                val child = seatLayout.getChildAt(row * seatLayout.columnCount + col)
//                if (child is TextView && child.visibility == View.VISIBLE) {
//                    reservedSeats.add(child.text.toString())
//                }
//            }
//        }
//        return reservedSeats
//    }
}

