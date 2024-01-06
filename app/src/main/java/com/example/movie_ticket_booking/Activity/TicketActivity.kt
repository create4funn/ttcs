package com.example.movie_ticket_booking.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.movie_ticket_booking.Adapter.TicketAdapter
import com.example.movie_ticket_booking.Adapter.ViewPagerAdapter
import com.example.movie_ticket_booking.Fragment.UnwatchedFragment
import com.example.movie_ticket_booking.Fragment.WatchedFragment
import com.example.movie_ticket_booking.Model.TicketItem
import com.example.movie_ticket_booking.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TicketActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_ticket)
        val btnBack = findViewById<ImageButton>(R.id.ticket_btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_ticket)
        val viewPager = findViewById<ViewPager2>(R.id.viewpager_ticket)

        val fragments = listOf(UnwatchedFragment(), WatchedFragment())
        val adapter = ViewPagerAdapter(this, fragments)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            when(position){
                0->{tab.text="Valid"}
                1->{tab.text="Expired"}
            }
        }.attach()
//        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
//        viewPager.adapter = adapter
//        TabLayoutMediator(tabLayout,viewPager){tab,position->
//            when(position){
//                0->{tab.text="Phim chưa xem"}
//                1->{tab.text="Phim đã xem"}
//            }
//        }.attach()
    }

    fun getTicket(uid: String, recyclerView: RecyclerView, isValid: Boolean) {
        val listTicket = mutableListOf<TicketItem>()

        // Lấy ngày giờ hiện tại
        val currentDate = Calendar.getInstance().time

        db.collection("tickets").whereEqualTo("user_id", uid).addSnapshotListener { value, error ->
            if (error != null) {
                // Xử lý lỗi nếu có
                return@addSnapshotListener
            }

            for (document in value!!) {
                val id = document.id
                val img = document.getString("img")
                val movieName = document.getString("movie_name")
                val date = document.getString("date")
                val showtime = document.getString("showtime")
                val theaterName = document.getString("theater_name")
                val food = document.getString("food")
                val seatArr = document["seat"] as ArrayList<String>?
                val seat = if (seatArr?.size!! > 1) {
                    seatArr.joinToString(", ")
                } else {
                    seatArr[0]
                }
                val total = document.getString("total")?.toLong()

//                val ticketData = TicketItem(img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
//                listTicket.add(ticketData)
                // Chuyển đổi date và showtime từ String sang Date
                var dateTimeString = "$date $showtime"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val showDateTime = dateFormat.parse(dateTimeString)

                // So sánh với ngày giờ hiện tại
                val isTicketValid = showDateTime?.after(currentDate) ?: false

                if (isValid && isTicketValid) {
                    // Nếu hàm được gọi từ valid ticket và vé còn hạn sử dụng, thêm vào danh sách
                    val ticketData = TicketItem(id, img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
                    listTicket.add(ticketData)
                } else if (!isValid && !isTicketValid) {
                    // Nếu hàm được gọi từ expired ticket và vé đã hết hạn, thêm vào danh sách
                    val ticketData = TicketItem(id, img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
                    listTicket.add(ticketData)
                }
            }
            recyclerView.layoutManager= LinearLayoutManager(this)
            val adapter = TicketAdapter(listTicket){
                AppData.selectedTicket = it
                val intent = Intent(this, TicketDetailActivity::class.java)
                intent.putExtra("check", isValid)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            Log.d("aac","${listTicket.size}")

        }


    }


}