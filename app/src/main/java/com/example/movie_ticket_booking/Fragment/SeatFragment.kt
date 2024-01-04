package com.example.movie_ticket_booking.Fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.view.setMargins
import com.example.movie_ticket_booking.Activity.AppData
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log


class SeatFragment(private val context: Context) : Fragment() {

    private val selectedSeats = mutableListOf<String>()
    private val seatList = mutableListOf<String>()
    private lateinit var db: FirebaseFirestore
    private val theater = AppData.selectedTheater
    private val showtime = AppData.selectedShowtime
    private val date = AppData.selectedDate.toString()
    private val movie = AppData.selectedMovie


    private lateinit var seatLayout: GridLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seatLayout = view.findViewById(R.id.gv_seatLayout)

//        Log.d("nnn","test")
        getReservedSeats()

    }

    private fun getReservedSeats() {
        db = FirebaseFirestore.getInstance()
        db.collection("tickets")
            .whereEqualTo("movie_name", movie?.name)
            .whereEqualTo("theater_name", theater?.theaterName)
            .whereEqualTo("showtime", showtime?.showtime)
            .whereEqualTo("date", date?.trim())
            .addSnapshotListener { value, error ->
                for (document in value!!) {
                    // Lấy giá trị của trường seat và thêm vào list
//                    val seat = document.getString("seat")
                    val seat = document["seat"] as ArrayList<String>?
                    seatList.addAll(seat!!)

                }
                initSeat()
                Toast.makeText(context, "test", Toast.LENGTH_LONG).show()
            }


    }

    fun initSeat(){
        val seatActivity = activity as SeatActivity
        val numRows = 8
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
                    setMargins(5)
                }

                val seatName = "$listRow$seatNumber"
                seatName.trim()


                if (!seatList.contains(seatName)) {
//                if (seatList.none { it == seatName }) {

                    seat.setBackgroundResource(R.drawable.seat_background)

                    // Biến đánh dấu trạng thái của Button
                    var isSeatSelected = false

                    seat.setOnClickListener {
                        if (isSeatSelected) {

                            // Nếu ghế đã được chọn, đặt lại màu ban đầu
                            seat.setBackgroundColor(Color.WHITE)
                            // Xóa ghế khỏi danh sách khi bỏ chọn
                            selectedSeats.remove(seat.text.toString())
                        } else {
                            // Nếu ghế chưa được chọn, đặt màu đỏ
                            seat.setBackgroundColor(Color.RED)

                            // Thêm ghế vào danh sách khi chọn
                            selectedSeats.add(seat.text.toString())
                        }

                        // Gọi phương thức để cập nhật danh sách ghế được chọn
                        seatActivity.updateSelectedSeat(selectedSeats)

                        // Đảo ngược trạng thái của biến đánh dấu
                        isSeatSelected = !isSeatSelected
                    }
                } else {

                    seat.setBackgroundColor(Color.GRAY)
                    seat.isEnabled = false
                }

                seatLayout.addView(seat)
            }
            listRow += 1
        }
    }
}