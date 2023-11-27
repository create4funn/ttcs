package com.example.movie_ticket_booking.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.movie_ticket_booking.Adapter.ViewPagerAdapter
import com.example.movie_ticket_booking.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_ticket)
        val viewPager = findViewById<ViewPager2>(R.id.viewpager_ticket)

        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager){tab,position->
            when(position){
                0->{tab.text="Phim chưa xem"}
                1->{tab.text="Phim đã xem"}
            }
        }
    }
}