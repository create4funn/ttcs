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
import java.util.*

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
    private var total: Long = 0
    private lateinit var db: FirebaseFirestore
    private val theater = AppData.selectedTheater
    private val showtime = AppData.selectedShowtime
    private val date = AppData.selectedDate.toString()
    private val movie = AppData.selectedMovie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movieseat)
        initView()
        btnBack.setOnClickListener {
            finish()
        }
        replaceFragment(SeatFragment(this))

        btnBook.setOnClickListener {

            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_seat)
            if (currentFragment is SeatFragment && check) {
                replaceFragment(FoodFragment())
            } else if (currentFragment is FoodFragment) {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("total", total)
                intent.putStringArrayListExtra("seatList", ArrayList(selectedSeatsList))
                intent.putStringArrayListExtra("foodList", ArrayList(selectedFoodsList))
                startActivity(intent)
                finish()
            } else{
                Toast.makeText(this, "bạn chua chọn ghế", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initView(){
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


    fun updateSelectedSeat(selectedSeats: List<String>){
        if (selectedSeats.isNotEmpty()){
            check = true
        }
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
    fun initFoodAdapter(recyclerView : RecyclerView, foodList: List<FoodItem>){
        val foodAdapter = FoodAdapter(this,this)
        recyclerView.adapter = foodAdapter
        foodAdapter.setData(foodList)
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

