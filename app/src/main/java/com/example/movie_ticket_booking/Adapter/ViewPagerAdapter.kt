package com.example.movie_ticket_booking.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movie_ticket_booking.Fragment.UnwatchedFragment
import com.example.movie_ticket_booking.Fragment.WatchedFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> {UnwatchedFragment()}
            else-> {WatchedFragment()}
        }
    }
}