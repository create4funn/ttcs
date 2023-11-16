package com.example.movie_ticket_booking.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.movie_ticket_booking.R
import com.example.movie_ticket_booking.RvAdapter
import com.example.movie_ticket_booking.listfilm
import java.util.*

//import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
//import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
//import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {
    private lateinit var calendarTextView: TextView
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var slider: ImageSlider

    private lateinit var imageSlider: ImageSlider

    private lateinit var recyclerView: RecyclerView
    private val listfilm = ArrayList<listfilm>()

    private lateinit var recyclerView1: RecyclerView
    private val listfilm1 = ArrayList<listfilm>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //slider qunảng cáo
        imageSlider = findViewById(R.id.img_main_slider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner2))
        imageList.add(SlideModel(R.drawable.banner3))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

// recycleview film đang chiếu
        recyclerView = findViewById(R.id.recyclerView_dangchieu_main)
       // recyclerView.layoutManager = LinearLayoutManager(this) // You can use LinearLayoutManager or GridLayoutManager

        val filmList = mutableListOf(
            listfilm(R.drawable.demkinhhoang, "Đêm kinh hoàng", "120 min"),
            listfilm(R.drawable.mynhandaochich, "Mỹ nhân đạo chích", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Người vợ cuối cùng", "120 min")
        )
        listfilm.addAll(filmList)
        val adapter = RvAdapter(listfilm)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

// recycleview film sắp chiếu
        recyclerView1 = findViewById(R.id.recyclerView_sapchieu_main)
      //  recyclerView1.layoutManager = LinearLayoutManager(this) // You can use LinearLayoutManager or GridLayoutManager

        val filmList1 = mutableListOf(
            listfilm(R.drawable.nguoivocuoicung, "Đại náo cung trăng", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
            listfilm(R.drawable.nguoivocuoicung, "The Dark Knight", "120 min")
        )
        listfilm1.addAll(filmList1)
        val adapter1 = RvAdapter(listfilm1)
        recyclerView1.adapter = adapter1
        recyclerView1.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        // navigation
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val btnMenu = findViewById<ImageButton>(R.id.btn_menu)

        // Khởi tạo ActionBarDrawerToggle để quản lý sự kiện mở/đóng của Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open, R.string.close
        )

        // Đăng ký ActionBarDrawerToggle với DrawerLayout
        drawerLayout.addDrawerListener(toggle)

        // Hiển thị Toggle
        toggle.syncState()

        // Đặt sự kiện click cho ImageButton "btn_menu"
        btnMenu.setOnClickListener {
                drawerLayout.openDrawer(findViewById(R.id.nav_view))
        }

        val btnAccount = findViewById<ImageButton>(R.id.btn_account)
        btnAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val viewmore = findViewById<TextView>(R.id.tv_viewmore)
        viewmore.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
//        val slider = findViewById<ImageSlider>(R.id.image_slider)
//
//        val imageList = ArrayList<SlideModel>()
//        imageList.add(SlideModel(R.drawable.nen))
//        imageList.add(SlideModel(R.drawable.mot))
//        imageList.add(SlideModel(R.drawable.nguoivocuoicung))
//
//        slider.setImageList(imageList, ScaleTypes.FIT)
//        calendarTextView = findViewById(R.id.cal_tv)
//
//
//        val startDate: Calendar = Calendar.getInstance()
//        startDate.add(Calendar.MONTH, -1)
//
//
//        val endDate: Calendar = Calendar.getInstance()
//        endDate.add(Calendar.MONTH, 1)
//
//        // Horizontal Calendar setup
//        val horizontalCalendar = HorizontalCalendar.Builder(this, R.id.calendar_view)
//            .range(startDate, endDate)
//            .datesNumberOnScreen(7)
//            .build()
//
//
//        year = startDate.get(Calendar.YEAR)
//        month = startDate.get(Calendar.MONTH) + 1
//        day = startDate.get(Calendar.DAY_OF_MONTH)
//        updateCalendarTextView()
//
//
//        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
//            override fun onDateSelected(date: Calendar, position: Int) {
//                year = date.get(Calendar.YEAR)
//                month = date.get(Calendar.MONTH) + 1
//                day = date.get(Calendar.DAY_OF_MONTH)
//                updateCalendarTextView()
//            }
//        }
    }

//    private fun updateCalendarTextView() {
//        calendarTextView.text = "$day/$month/$year "
//    }
}
