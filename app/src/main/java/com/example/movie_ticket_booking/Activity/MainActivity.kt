package com.example.movie_ticket_booking.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.movie_ticket_booking.R
import com.example.movie_ticket_booking.Adapter.MovieAdapter
import com.example.movie_ticket_booking.Model.MovieItem
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity() {


    private lateinit var imageSlider: ImageSlider
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var profile_image: ImageView
    private lateinit var profile_email: TextView
    private lateinit var profile_username: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnMenu:ImageButton
    private lateinit var btnAccount:ImageButton
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mNavigationView: NavigationView
    private var db = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initUi()
        // navigation

        navigation()

        //slider qunảng cáo

        slider()

        showUserInformation()





        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView2.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        db.collection("movies").addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Xử lý lỗi nếu có
                return@addSnapshotListener
            }
            val listNowShowing = mutableListOf<MovieItem>()
            val listComingSoon = mutableListOf<MovieItem>()
            val curentDate = Date()

            for (document in snapshot!!) {

                val startDateString = document.getString("start")
                val endDateString = document.getString("end")
                val startDate = SimpleDateFormat("dd/MM/yyyy").parse(startDateString)
                val endDate = SimpleDateFormat("dd/MM/yyyy").parse(endDateString)

                if(startDate <= curentDate && curentDate <= endDate){
                    val movie = getMovieItemFromDocument(document)
                    listNowShowing.add(movie)
                } else if(startDate > curentDate) {
                    // Phim sắp khởi chiếu
                    val movie = getMovieItemFromDocument(document)
                    listComingSoon.add(movie)
                }
            }
            val adapterNowShowing = MovieAdapter { selectedMovie ->
                AppData.selectedMovie = selectedMovie
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("check", true)
                startActivity(intent)
            }
            recyclerView.adapter = adapterNowShowing
            adapterNowShowing.setData(listNowShowing)

            val adapterComingSoon = MovieAdapter { selectedMovie ->
                AppData.selectedMovie = selectedMovie
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("check", false)
                startActivity(intent)
            }
            recyclerView2.adapter = adapterComingSoon
            adapterComingSoon.setData(listComingSoon)
        }






    }

    private fun slider() {
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner2))
        imageList.add(SlideModel(R.drawable.banner3))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun navigation() {
        // Khởi tạo ActionBarDrawerToggle để quản lý sự kiện mở/đóng của Navigation Drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open, R.string.close
        )

        // Đăng ký ActionBarDrawerToggle với DrawerLayout
        drawerLayout.addDrawerListener(toggle)

        // Hiển thị Toggle
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Đặt sự kiện click cho ImageButton "btn_menu"
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(mNavigationView)
        }

        mNavigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_ticket -> {
                    val intent = Intent(this, TicketActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_account -> {
                    val intent = Intent(this, AccountActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    firebaseAuth.signOut()
                    Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }

            }
            false
        }
        btnAccount.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }



    }

    private fun initUi() {
        recyclerView = findViewById(R.id.recyclerView_dangchieu_main)
        recyclerView2 = findViewById(R.id.recyclerView_sapchieu_main)
        btnAccount = findViewById(R.id.btn_account)
        drawerLayout = findViewById(R.id.drawer_layout)
        btnMenu = findViewById(R.id.btn_menu)
        mNavigationView = findViewById(R.id.nav_view)
        imageSlider = findViewById(R.id.img_main_slider)
        profile_username = mNavigationView.getHeaderView(0).findViewById(R.id.profile_username)
        profile_image = mNavigationView.getHeaderView(0).findViewById(R.id.profile_image)
        profile_email = mNavigationView.getHeaderView(0).findViewById(R.id.profile_email)
    }

    private fun showUserInformation() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        if (user != null) {
            // Use fstore to access the Firestore database
            db.collection("Users").document(userID).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username: String? = documentSnapshot.getString("Username")
                        val email: String? = user.email
                        val photoUrl: Uri? = user.photoUrl

                        profile_email.text = email
                        profile_username.text = username

                      //  Glide.with(this)
                       //     .load(photoUrl)
                       //     .error(R.drawable.catavata)
                        //    .into(profile_image)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failures
                }
        }
    }


    private fun getMovieItemFromDocument(document: DocumentSnapshot): MovieItem {
        val id = document.id
        val name = document.getString("name")
        val duration = document.getString("duration")
        val img = document.getString("img")
        val castArray = document["cast"] as ArrayList<String>?
        val cast = if (castArray?.size!! > 1) {
            castArray.joinToString(", ")
        } else {
            castArray[0]
        }
        val director = document.getString("director")
        val summary = document.getString("summary")
        val trailer = document.getString("trailer")
        val age = document.getString("age")
        val price = document.getLong("price")
        val genre = document.getString("genre")

        return MovieItem(id, img!!, name!!, duration!!, age!!, cast, director!!, price!!, summary!!, trailer!!, genre!!)
    }
}


