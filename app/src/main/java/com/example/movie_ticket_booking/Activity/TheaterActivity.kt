package com.example.movie_ticket_booking.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import java.util.*
import kotlin.collections.ArrayList

class TheaterActivity : AppCompatActivity() {
    private lateinit var calendarTextView: TextView
    private lateinit var db : FirebaseFirestore
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


//        val showtimelist : List<ShowtimeItem> = listOf(
//            ShowtimeItem("1","10:10"),ShowtimeItem("1","20:10"),ShowtimeItem("1","22:10"),ShowtimeItem("1","20:10"),ShowtimeItem("1","20:10")
//        )
//        val theaterlist : List<TheaterItem> = listOf(
//            TheaterItem("Hồ Gươm", showtimelist),
//            TheaterItem("Mac Plaza", showtimelist),
//            TheaterItem("Hồ Gươm", showtimelist),
//            TheaterItem("Mac Plaza", showtimelist)
//        )

        db = FirebaseFirestore.getInstance()
        val rv_theater = findViewById<RecyclerView>(R.id.rv_theater)
        //val adapter = TheaterAdapter(theaterlist)
        //rv_theater.adapter = adapter
        rv_theater.layoutManager = LinearLayoutManager(this)

        //fetchDataFromFirestore()

        val selectedMovie = AppData.selectedMovie
        val movieId = selectedMovie?.id




        db.collection("theaters")
            .addSnapshotListener { value, e ->
                Log.e("ooo", "fffffff")
                if (e != null) {
                    // Xử lý lỗi nếu có
                    return@addSnapshotListener
                }

                if (value != null) {
                    val theaterList = mutableListOf<TheaterItem>()

                    for (document in value) {
                        val name = document.getString("name")
                        val theaterId = document.id
                        val address = document.getString("location")

                        // Truy cập subcollection "showTimes" của mỗi rạp
                        val showTimesCollection = document.reference.collection("showtime")
                        // Truy cập document trong subcollection
                        showTimesCollection.get()
                            .addOnCompleteListener { showTimesSnapshot ->
                                if (showTimesSnapshot.isSuccessful) {
                                    val showtimeList = mutableListOf<ShowtimeItem>()

                                    for (showTimesDocument in showTimesSnapshot.result!!) {
                                        // Lấy dữ liệu từ sub document
                                        val mid = showTimesDocument.id
                                        if (mid == movieId) {
                                            Log.e("qqq", "bằng")
                                            val data: Map<String, Any> = showTimesDocument.data
                                            val keys: List<String> = data.keys.toList()

                                            // Lặp qua danh sách khóa theo thứ tự ngược lại
                                            for (i in keys.indices.reversed()) {
                                                val field = keys[i]
                                                val value = data[field]
                                                Log.e("qqq", "$field $value")
                                                val showtimeData =
                                                    ShowtimeItem(field, value as String)
                                                showtimeList.add(showtimeData)
                                                val length = showtimeList.size
                                                Log.d("qqq", "$length")
                                            }
                                        }
                                    }

                                    // Sau khi xử lý dữ liệu showTimes, thêm vào theaterList
                                    val theaterData =
                                        TheaterItem(theaterId, name!!, address!!, showtimeList)
                                    theaterList.add(theaterData)

                                    // Kiểm tra kích thước theaterList sau khi thêm dữ liệu
                                    val length = theaterList.size
                                    Log.e("qqq", "$length")

                                    // Kiểm tra kích thước theaterList để xem liệu dữ liệu có được giữ lại hay không
                                    val adapter = TheaterAdapter(this)
                                    rv_theater.adapter = adapter
                                    adapter.setData(theaterList)
                                } else {
                                    // Xử lý lỗi khi truy cập subcollection
                                    Log.e(
                                        "ooo",
                                        "Lỗi khi truy cập subcollection",
                                        showTimesSnapshot.exception
                                    )
                                }
                        }
                    }
                }
            }

//        db.collection("theaters")
//            .addSnapshotListener { value, e ->
//                if (e != null) {
//                    // Xử lý lỗi nếu có
//                    return@addSnapshotListener
//                }
//
//                val theaterList = mutableListOf<TheaterItem>()
//
//
//
//                for (document in value!!) {
//                    val id = document.id
//                    val name = document.getString("name") ?: ""
//                    val address = document.getString("location") ?: ""
//                    val showtimeList = mutableListOf<ShowtimeItem>()
//                    val showTimesRef = db.collection("theaters").document().collection("showtime")
//                    showTimesRef.get()
//                        .addOnSuccessListener { subQuerySnapshot ->
//                        Log.e("bbb","1111111")
//                        if (!subQuerySnapshot.isEmpty) {
//                            Log.e("bbb","not empty")
//                            val subDocument = subQuerySnapshot.documents[0]
//
//                            for ((fieldName, fieldValue) in subDocument.data?.entries!!) {
//                                val showtimeId = fieldName
//                                val fieldValue = fieldValue
//
//                                val myData = ShowtimeItem(showtimeId, fieldValue as String)
//                                showtimeList.add(myData)
//                            }
//                        }
//
//                    }.addOnFailureListener { exception ->
//                        // Xử lý khi truy vấn thất bại
//                    }
//                    val theaterData = TheaterItem(id, name, address, showtimeList)
//                    theaterList.add(theaterData)
//
//
//                }
//                val adapter = TheaterAdapter()
//                rv_theater.adapter = adapter
//                adapter.setData(theaterList)
//            }







//        db.collection("theaters")
////            .whereEqualTo("showTimes.$movieId", true)
//            .addSnapshotListener { value, e ->
//            if (e != null) {
//                // Xử lý lỗi nếu có
//                return@addSnapshotListener
//            }
//
//            val theaterList = mutableListOf<TheaterItem>()
//
//            for(document in value!!){
//                var id =""
//                var name =""
//                var address =""
//                val showtimeList = mutableListOf<ShowtimeItem>()
//                val showTimesRef = document.reference.collection("showtime")
//                showTimesRef.whereEqualTo(movieId!!, true)
//                    .get()
//                    .addOnSuccessListener { subQuerySnapshot ->
//                        if (!subQuerySnapshot.isEmpty) {
//                            val subDocument = subQuerySnapshot.documents[0]
//                            // Lấy dữ liệu name và address của collection lớn
//                             id = document.id
//                             name = document.getString("name").toString()
//                             address = document.getString("location").toString()
//
//
//                            for ((fieldName, fieldValue) in subDocument.data!!.entries) {
//
//                                val showtimeId = fieldName
//                                val fieldValue = fieldValue
//                                val myData = ShowtimeItem(showtimeId, fieldValue as String)
//                                showtimeList.add(myData)
//                            }
//
//
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        // Xử lý khi truy vấn thất bại
//                    }
//
////                val id = document.id
////                val name = document.getString("name")
////                val address = document.getString("location")
//
////                val showtime = document.get("showtime") as Map<String, Map<String, String>>?
////                val showtimeList = mutableListOf<ShowtimeItem>()
////                if (showtime != null) {
////                    for (showtimeMap in showtime) {
////
////                            val id = showtimeMap
////                            val value = timeValue
////                            val myData = ShowtimeItem(id,value)
////                            showtimeList.add(myData)
////
////                        // Xử lý giá trị của các field nằm trong sub document
////
////                    }
////                }
//                val theaterData = TheaterItem(id,name!!,address!!,showtimeList)
//                theaterList.add(theaterData)
//            }
//                val adapter = TheaterAdapter()
//                rv_theater.adapter = adapter
//                adapter.setData(theaterList)
//        }












//        db.collection("theaters")
//            .addSnapshotListener { documents, e ->
//            if (e != null) {
//                // Xử lý lỗi nếu có
//                return@addSnapshotListener
//            }
//                val dataList = mutableListOf<TheaterItem>()
//                for (document in documents!!) {
//                    val id = document.id
//                    val name = document.getString("name")
//                    val address = document.getString("location")
//                    val myData = TheaterItem(id,name!!,address!!, showtimelist)
//                    dataList.add(myData)
//                }
//
//                val adapter = TheaterAdapter()
//                rv_theater.adapter = adapter
//                adapter.setData(dataList)
//            }




    }
    private fun updateCalendarTextView() {
        calendarTextView.text = "$day/$month/$year "
    }


}