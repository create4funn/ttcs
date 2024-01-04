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
import com.example.movie_ticket_booking.Activity.SeatActivity
import com.example.movie_ticket_booking.Activity.TicketActivity
import com.example.movie_ticket_booking.Adapter.TicketAdapter
import com.example.movie_ticket_booking.Model.TicketItem
import com.example.movie_ticket_booking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class UnwatchedFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unwatched, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fm_unwatched)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val ticketActivity = activity as TicketActivity
        ticketActivity.getTicket(auth.currentUser!!.uid,recyclerView,true)
//
        Log.d("aac","1")
//        recyclerView.layoutManager= LinearLayoutManager(context)
//        val adapter = TicketAdapter(ticketList){
//            Toast.makeText(requireContext(), "clicked ", Toast.LENGTH_LONG).show()
//        }
//        recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()

//        val listTicket = mutableListOf<TicketItem>()
//
//        // Lấy ngày giờ hiện tại
//        val currentDate = Calendar.getInstance().time
//
//        db.collection("tickets")
////            .whereEqualTo("user_id", auth.currentUser?.uid)
//            .addSnapshotListener { value, error ->
//            if (error != null) {
//                // Xử lý lỗi nếu có
//                return@addSnapshotListener
//            }
//
//                Log.d("aac","2")
//            for (document in value!!) {
//                val img = document.getString("img")
//                val movieName = document.getString("movie_name")
//                val date = document.getString("date")
//                val showtime = document.getString("showtime")
//                val theaterName = document.getString("theater_name")
//                val food = document.getString("food")
//                val seatArr = document["seat"] as ArrayList<String>?
//                val seat = if (seatArr?.size!! > 1) {
//                    seatArr.joinToString(", ")
//                } else {
//                    seatArr[0]
//                }
//                val total = document.getString("total")
//
//                val ticketData = TicketItem(img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
//                listTicket.add(ticketData)
////                // Chuyển đổi date và showtime từ String sang Date
////                var dateTimeString = "$date $showtime"
////                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
////                val showDateTime = dateFormat.parse(dateTimeString)
////
////                // So sánh với ngày giờ hiện tại
////                val isTicketValid = showDateTime?.after(currentDate) ?: false
////
////                if (isValid && isTicketValid) {
////                    // Nếu hàm được gọi từ valid ticket và vé còn hạn sử dụng, thêm vào danh sách
////                    val ticketData = TicketItem(img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
////                    listTicket.add(ticketData)
////                } else if (!isValid && !isTicketValid) {
////                    // Nếu hàm được gọi từ expired ticket và vé đã hết hạn, thêm vào danh sách
////                    val ticketData = TicketItem(img!!, movieName!!, theaterName!!, date!!, showtime!!, seat!!, food, total!!)
////                    listTicket.add(ticketData)
////                }
//            }
//                recyclerView.layoutManager= LinearLayoutManager(requireContext())
//                val adapter = TicketAdapter(listTicket){
//                    Toast.makeText(requireContext(), "clicked ", Toast.LENGTH_LONG).show()
//                }
//                recyclerView.adapter = adapter
//                Log.d("aac","${listTicket.size}")
//        }


//        recyclerView.layoutManager= LinearLayoutManager(requireContext())
//        val adapter = TicketAdapter(listTicket){
//            Toast.makeText(requireContext(), "clicked ", Toast.LENGTH_LONG).show()
//        }
//        recyclerView.adapter = adapter
//        Log.d("aac","3")
    }


}