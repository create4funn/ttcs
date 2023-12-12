package com.example.movie_ticket_booking.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
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

        val btnBack = findViewById<ImageView>(R.id.btn_back_seat)
        btnBack.setOnClickListener {
            finish()
        }
        val movie = AppData.selectedMovie
        val tv_actionbar = findViewById<TextView>(R.id.tv_actionBar)
        tv_actionbar.text = movie?.name
        replaceFragment(SeatFragment())
        val btnbook = findViewById<Button>(R.id.btn_seat_book)
        btnbook.setOnClickListener {

            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_seat)

            if (currentFragment is FoodFragment) {
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            } else {

                replaceFragment(FoodFragment())
            }
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

