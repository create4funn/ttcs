package com.example.movie_ticket_booking.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_ticket_booking.Activity.TicketActivity
import com.example.movie_ticket_booking.Adapter.TicketAdapter
import com.example.movie_ticket_booking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class WatchedFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watched, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fm_watched)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val ticketActivity = activity as TicketActivity
        ticketActivity.getTicket(auth.currentUser!!.uid,recyclerView,false)
//
        Log.d("aac","1")
    }

}