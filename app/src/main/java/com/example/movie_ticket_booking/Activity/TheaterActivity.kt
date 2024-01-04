package com.example.movie_ticket_booking.Activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Adapter.TheaterAdapter
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.R
import com.google.firebase.firestore.FirebaseFirestore
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.text.SimpleDateFormat
import java.util.*

class TheaterActivity : AppCompatActivity() {
    private lateinit var calendarTextView: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var tv_actionbar: TextView
    private lateinit var rv_theater: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var year = 0
    private var month = 0
    private var day = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choosetheater)

        initView()
        setupCalendar()
        fetch()

    }

    private fun initView() {
        btnBack = findViewById(R.id.btn_back_theater)
        btnBack.setOnClickListener {
            finish()
        }
        val movie = AppData.selectedMovie
        tv_actionbar = findViewById(R.id.tv_actionBar)
        tv_actionbar.text = movie?.name

        calendarTextView = findViewById(R.id.cal_tv)

        rv_theater = findViewById(R.id.rv_theater)
    }

    private fun setupCalendar() {
        val startDate: Calendar = Calendar.getInstance()
        updateCalendarTextView(startDate)
        startDate.add(Calendar.MONTH, -1)

        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, +1)

        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendar_view)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                updateCalendarTextView(date)
                fetch()
            }
        }
    }

    private fun updateCalendarTextView(date: Calendar) {
        year = date.get(Calendar.YEAR)
        month = date.get(Calendar.MONTH) + 1
        day = date.get(Calendar.DAY_OF_MONTH)
        calendarTextView.text = "$day/$month/$year "
    }


    private fun fetch() {
        db = FirebaseFirestore.getInstance()
        rv_theater.layoutManager = LinearLayoutManager(this)

        val selectedMovie = AppData.selectedMovie
        val movieId = selectedMovie?.id

        db.collection("theaters")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    // Xử lý lỗi nếu có
                    return@addSnapshotListener
                }

                val theaterList = mutableListOf<TheaterItem>()

                for (document in value!!) {
                    val name = document.getString("name")
                    val theaterId = document.id
                    val address = document.getString("location")

                    // Truy cập subcollection "showtime"
                    val showTimesCollection = document.reference.collection("showtime")
                    // Truy cập document trong subcollection
                    showTimesCollection.get()
                        .addOnCompleteListener { showTimesSnapshot ->
                            val showtimeList = mutableListOf<ShowtimeItem>()
                            for (showTimesDocument in showTimesSnapshot.result!!) {
                                // Lấy dữ liệu từ sub document
                                val mid = showTimesDocument.id
                                if (mid == movieId) {
                                    val data: Map<String, Any> = showTimesDocument.data
                                    val values: List<String> =
                                        data.values.map { it.toString() }.sorted()

                                    // Lặp qua danh sách các giá trị
                                    for (value in values) {
                                        if (checkShowtime(value, day, month, year)) {
                                            // Lấy tên field tương ứng với giá trị
                                            val field =
                                                data.keys.first { data[it].toString() == value }

                                            val showtimeData = ShowtimeItem(field, value)
                                            showtimeList.add(showtimeData)
                                        }
                                    }
                                }

                            }

                            if (showtimeList.isNotEmpty()) {
                                val theaterData =
                                    TheaterItem(theaterId, name!!, address!!, showtimeList)
                                theaterList.add(theaterData)
                            }
                            val adapter = TheaterAdapter(theaterList, calendarTextView.text.toString())
                            rv_theater.adapter = adapter
                            adapter.notifyDataSetChanged()

                        }

                }
            }
    }


    fun checkShowtime(showtime: String, day: Int, month: Int, year: Int): Boolean {

        val currentTime = Calendar.getInstance()
        //check ngày tháng được chọn với ngày hiện tại
        if (currentTime.get(Calendar.DAY_OF_MONTH) == day && currentTime.get(Calendar.MONTH) + 1 == month) {

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            try {
                val currentTimeString = dateFormat.format(currentTime.time)
                val time = dateFormat.parse(showtime)
                val currentTimeParsed = dateFormat.parse(currentTimeString)

                return currentTimeParsed.before(time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if ((currentTime.get(Calendar.DAY_OF_MONTH) > day || currentTime.get((Calendar.MONTH)) + 1 > month)
            && currentTime.get((Calendar.YEAR)) == year) {
            return false
        }

        return true
    }
}