package com.example.movie_ticket_booking.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movie_ticket_booking.Fragment.UnwatchedFragment
import com.example.movie_ticket_booking.Fragment.WatchedFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment{
        return fragments[position]
    }
}