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
import com.example.movie_ticket_booking.Adapter.MovieAdapter
import com.example.movie_ticket_booking.Model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

//import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
//import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
//import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    private lateinit var slider: ImageSlider

    private lateinit var imageSlider: ImageSlider

    private lateinit var recyclerView: RecyclerView
    private val MovieItem = ArrayList<MovieItem>()

    private lateinit var recyclerView1: RecyclerView
    private val movieItem1 = ArrayList<MovieItem>()

    private lateinit var db:FirebaseFirestore
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

//        val filmList = mutableListOf(
//            MovieItem(R.drawable.demkinhhoang, "Đêm kinh hoàng", "120 min"),
//            MovieItem(R.drawable.mynhandaochich, "Mỹ nhân đạo chích", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Người vợ cuối cùng", "120 min")
//        )
//        MovieItem.addAll(filmList)

        db = FirebaseFirestore.getInstance()

//        val movieAdapter = MovieAdapter()
//        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        db.collection("movies").addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Xử lý lỗi nếu có
                return@addSnapshotListener
            }
            val movieList = mutableListOf<MovieItem>()

            for (document in snapshot!!) {
                val id = document.id
                val name = document.getString("name")
                val duration = document.getString("duration")
                val img = document.getString("img")
                val cast = document.getString("cast")
                val director = document.getString("director")
                val summary = document.getString("summary")
                val trailer = document.getString("trailer")
                val age = document.getString("age")
                val price = document.getLong("price")
                val genre = document.getString("genre")

                val myData = MovieItem(id, img!!, name!!, duration!!,age!!,cast!!,director!!, price!!, summary!!, trailer!!, genre!!)
                movieList.add(myData)

            }
            val movieAdapter = MovieAdapter { selectedMovie ->
                AppData.selectedMovie = selectedMovie
                val intent = Intent(this, DetailActivity::class.java)
                startActivity(intent)
            }
            recyclerView.adapter = movieAdapter
            movieAdapter.setData(movieList)
        }



// recycleview film sắp chiếu
//        recyclerView1 = findViewById(R.id.recyclerView_sapchieu_main)
//      //  recyclerView1.layoutManager = LinearLayoutManager(this) // You can use LinearLayoutManager or GridLayoutManager
//
//        val filmList1 = mutableListOf(
//            MovieItem(R.drawable.nguoivocuoicung, "Đại náo cung trăng", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "Đấu trường sinh tử", "120 min"),
//            MovieItem(R.drawable.nguoivocuoicung, "The Dark Knight", "120 min")
//        )
//        movieItem1.addAll(filmList1)
//        val adapter1 = RvAdapter(movieItem1)
//        recyclerView1.adapter = adapter1
//        recyclerView1.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )


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

    }


}


