package com.example.movie_ticket_booking.Activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Activity.AppData.selectedTheater
import com.example.movie_ticket_booking.Adapter.TheaterAdapter
import com.example.movie_ticket_booking.Model.ShowtimeItem
import com.example.movie_ticket_booking.Model.TheaterItem
import com.example.movie_ticket_booking.R
import com.example.movie_ticket_booking.databinding.ChoosetheaterBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TheaterActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var calendarTextView: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var tv_actionbar: TextView
    private lateinit var rv_theater: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var mMap: GoogleMap
    private var currentLocation: Location = Location("default")
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient //đây là cái j
    private lateinit var locatv: ImageButton
    private lateinit var spacett: TextView
    private lateinit var binding: ChoosetheaterBinding
    private var selectedTheater: TheaterItem? = null
    private var FINE_PERMISSION_CODE = 1
    private var check = false
    private var year = 0
    private var month = 0
    private var day = 0

    private var curenlat = 0.0
    private var curenlong = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChoosetheaterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initView()
        setupCalendar()
//        fetch()

    }

    private fun initView() {
        // Use binding object to access views directly
        btnBack = binding.btnBackTheater
        locatv = binding.locatxt
        tv_actionbar = binding.tvActionBar
        calendarTextView = binding.calTv
        rv_theater = binding.rvTheater


        val movie = AppData.selectedMovie
        tv_actionbar.text = movie?.name

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locatv.setImageResource(R.drawable.baseline_location_on_24)
            check = true
            getLastLocation()
            fetch()
        }

        locatv.setOnClickListener{
            locatv.setImageResource(R.drawable.baseline_location_on_24)
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Nếu có quyền, lấy vị trí ngay lập tức
                getLastLocation()
                fetch()
            } else {
                // Nếu chưa có quyền, yêu cầu người dùng cấp quyền
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }

        }

        btnBack.setOnClickListener {
            finish()
        }


    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    curenlat = location.latitude
                    curenlong = location.longitude

                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu người dùng cấp quyền, lấy vị trí


                check = true
                getLastLocation()
                fetch()

            } else {
                // Nếu người dùng từ chối cấp quyền, xử lý tương ứng
                Toast.makeText(this, "Location is denied, please allow", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        getLastLocation()
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
        calendarTextView.text = "$day/$month/$year"
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetch() {
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
                    val maptt = document.getGeoPoint("map")


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
                                val distanceResults = FloatArray(1)
                                Location.distanceBetween(
                                    curenlat,
                                    curenlong,
                                    maptt!!.latitude,
                                    maptt!!.longitude,
                                    distanceResults
                                )
                                //vị trí hiện tại = 0, bật vị trí cũng ko nhận
                                Log.d(
                                    "abc",
                                    "${maptt.latitude}  ${maptt.longitude} ${curenlat} ${curenlong}"
                                )
                                val distanceInKilometers = distanceResults[0] / 1000.0
                                val roundedDistance =
                                    String.format("%.1f", distanceInKilometers).replace(',', '.')

                                val theaterData = TheaterItem(
                                    theaterId,
                                    name!!,
                                    address!!,
                                    showtimeList,
                                    maptt!!,
                                    roundedDistance.toDouble()
                                )
                                theaterList.add(theaterData)
                            }

                            val sortedList = theaterList.sortedBy { it.spacetv }
                            val adapter =
                                TheaterAdapter(sortedList, calendarTextView.text.toString(), check)
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
            && currentTime.get((Calendar.YEAR)) == year
        ) {
            return false
        }
        return true
    }


}