package com.example.movie_ticket_booking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Adapter.FoodAdapter
import com.example.movie_ticket_booking.Fragment.FoodFragment
import com.example.movie_ticket_booking.Fragment.SeatFragment
import com.example.movie_ticket_booking.Interface.FoodItemClickListener
import com.example.movie_ticket_booking.Model.FoodItem
import com.example.movie_ticket_booking.R
import com.google.firebase.firestore.FirebaseFirestore

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SeatActivity : AppCompatActivity(), FoodItemClickListener {
    private lateinit var btnBack: ImageButton
    private lateinit var tv_actionbar: TextView
    private lateinit var btnBook: Button
    private lateinit var selectedSeat: TextView
    private lateinit var selectedFood: TextView
    private lateinit var price: TextView
    private lateinit var movieName: TextView
    private var ticketPrice: Long = 0
    private val selectedSeatsList = mutableListOf<String>()
    private val selectedFoodsList = mutableListOf<String>()
    private var check: Boolean = false
    private var checkState: Boolean = true
    private var total: Long = 0
    private val db = FirebaseFirestore.getInstance()
    private val theater = AppData.selectedTheater
    private val showtime = AppData.selectedShowtime
    private val date = AppData.selectedDate.toString()
    private val movie = AppData.selectedMovie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movieseat)

        Log.d("bvb", "$checkState")
        initView()
        btnBack.setOnClickListener {
            finish()
        }


        replaceFragment(SeatFragment())

        btnBook.setOnClickListener {
            checkState()
            val currentFragment =
                supportFragmentManager.findFragmentById(R.id.frame_layout_seat)

            if (currentFragment is SeatFragment && check) {

                replaceFragment(FoodFragment())
                removeFragment(SeatFragment())
            } else if (!check) {
                Toast.makeText(this@SeatActivity, "bạn chua chọn ghế", Toast.LENGTH_LONG).show()
            }

            if (currentFragment is FoodFragment) {

                if (checkState) {
                    Log.d("bvb", "$checkState")
                    val data = hashMapOf(
                        "movie_name" to "${movie?.name}",
                        "seat" to ArrayList(selectedSeatsList),
                        "showtime" to "${showtime?.showtime}"
                    )
                    db.collection("state").document(theater!!.id)
                        .collection(convertDateFormat(date)).add(data)

                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("check", checkState)
                    intent.putExtra("total", total)
                    intent.putStringArrayListExtra("seatList", ArrayList(selectedSeatsList))
                    intent.putStringArrayListExtra("foodList", ArrayList(selectedFoodsList))
                    finish()
                    startActivity(intent)


                }else{
                    Toast.makeText(this,"Ghế đã được chọn", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    fun convertDateFormat(inputDate: String): String {
        try {
            // Định dạng đầu vào
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Định dạng đầu ra
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            // Chuyển đổi chuỗi ngày từ định dạng đầu vào sang định dạng đầu ra
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            // Xử lý nếu có lỗi, ví dụ: in ra log hoặc thông báo lỗi
            e.printStackTrace()
            return inputDate // Trả về ngày không thay đổi nếu có lỗi
        }
    }

    private fun initView() {
        btnBack = findViewById(R.id.btn_back_seat)
        tv_actionbar = findViewById(R.id.tv_actionBar)
        btnBook = findViewById(R.id.btn_seat_book)
        selectedSeat = findViewById(R.id.tv_seat_name)
        price = findViewById(R.id.tv_seat_total)
        movieName = findViewById(R.id.tv_seat_moviename)
        selectedFood = findViewById(R.id.tv_food_name)

        tv_actionbar.text = movie?.name
        movieName.text = movie?.name
        ticketPrice = movie?.price!!
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_seat, fragment).commit()
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    fun updateSelectedSeat(selectedSeats: List<String>) {
        check = selectedSeats.isNotEmpty()
        // Cập nhật danh sách và hiển thị lên TextView
        selectedSeatsList.clear()
        selectedSeatsList.addAll(selectedSeats)
        selectedSeat.text = "${selectedSeats.joinToString(",")}"

        total = selectedSeats.size * ticketPrice
        price.text = "${formatCurrency(total)}"

    }

    override fun onFoodItemClicked(selectedFoods: List<String>, foodPrice: Int) {
//        updateSelectedFood(selectedFood, foodPrice)
        selectedFoodsList.clear()
        selectedFoodsList.addAll(selectedFoods)
        selectedFood.text = if (selectedFoods.isNotEmpty()) {
            "+" + selectedFoods.joinToString(" +")
        } else {
            ""
        }

        total += foodPrice
        price.text = "${formatCurrency(total)}"

    }

    fun formatCurrency(amount: Long): String {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return formatter.format(amount) + "đ"
    }

    fun initFoodAdapter(recyclerView: RecyclerView, foodList: List<FoodItem>) {
        val foodAdapter = FoodAdapter(this, this)
        recyclerView.adapter = foodAdapter
        foodAdapter.setData(foodList)
    }


    private fun checkState() {


        db.collection("state").document(theater!!.id).collection(convertDateFormat(date))
            .whereEqualTo("showtime", showtime?.showtime)
            .whereEqualTo("movie_name", movie?.name)
            .whereArrayContainsAny("seat", selectedSeatsList)
            .get().addOnSuccessListener { documents ->
                for (document in documents ){
                    val seat = document["seat"] as ArrayList<String>?
                    Log.d("bvb", "ghe $seat")

                    checkState = false

                }

                Log.d("bvb","check $checkState")

            }

    }


    private fun checkTickets(): Boolean {
        var check = true
        db.collection("ticket")
            .whereEqualTo("movie_name", movie)
            .whereEqualTo("theater_name", theater)
            .whereEqualTo("date", date)
            .whereEqualTo("showtime", showtime)
            .whereArrayContainsAny("seat", selectedSeatsList)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    check = false
                }
            }
        return check
    }

//    fun getReservedSeats() : MutableList<String>{
//        val seatList = mutableListOf<String>()
//
//        db = FirebaseFirestore.getInstance()
//        db.collection("tickets")
//            .whereEqualTo("movie_name", movie?.name)
//            .whereEqualTo("theater_name", theater?.theaterName)
//            .whereEqualTo("showtime", showtime?.showtime)
////            .whereEqualTo("date", date)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                for (document in querySnapshot) {
//                    // Lấy giá trị của trường seat và thêm vào list
//                    val seat = document.getString("seat")
//                    if (seat != null) {
//                        seatList.add(seat)
//                    }
//                }
//                Log.d("acb","${seatList.size} ${movie?.name} ${theater?.theaterName} ${showtime?.showtime} $date")
//            }
//            return seatList
//
//    }


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

