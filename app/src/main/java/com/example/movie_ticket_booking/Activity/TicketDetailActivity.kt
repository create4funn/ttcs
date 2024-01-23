package com.example.movie_ticket_booking.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.movie_ticket_booking.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class TicketDetailActivity : AppCompatActivity() {
    private lateinit var img:ImageView
    private lateinit var qrCode:ImageView
    private lateinit var movieName:TextView
    private lateinit var theaterName:TextView
    private lateinit var time:TextView
    private lateinit var seat:TextView
    private lateinit var food:TextView
    private lateinit var total:TextView
    private lateinit var btnBack: ImageButton
    private lateinit var mapbtn: Button
    private var latt: Double = 0.0
    private var long: Double = 0.0

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val ticket = AppData.selectedTicket

        initial()

        db.collection("theaters")
            .whereEqualTo("theater_name",ticket?.theater_name).get().addOnSuccessListener {
                for (document in it){
                    val map = document.getGeoPoint("map")
                    latt = map!!.latitude
                    long = map.longitude
                }

            }
        val check = intent.getBooleanExtra("check",false)
        if(check){
            try {
                // Sử dụng MultiFormatWriter để tạo mã QR code từ dữ liệu
                val multiFormatWriter = MultiFormatWriter()
                val bitMatrix = multiFormatWriter.encode(ticket?.ticketID, BarcodeFormat.QR_CODE, 500, 500)

                // Sử dụng BarcodeEncoder để chuyển đổi bitMatrix thành Bitmap
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

                // Hiển thị Bitmap (QR code) trong ImageView
                qrCode.setImageBitmap(bitmap)

            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        Picasso.get().load(ticket?.imgMovie).into(img)
        movieName.text = ticket?.movie_name
        theaterName.text = "CGV "+ticket?.theater_name
        time.text = "${ticket?.hour}, ${ticket?.date}"
        seat.text = "Seats: "+ticket?.seat
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        total.text = "Total: "+formatter.format(ticket?.total).toString() +"đ"
        food.text = ticket?.food


        btnBack.setOnClickListener {
            finish()
        }

        mapbtn.setOnClickListener {

            val uri = Uri.parse("geo:$latt,$long?q=CGV ${ticket?.theater_name}")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
//            val uri = Uri.parse("https://www.google.com/maps/place/CGV " + ticket?.theater_name +"/")
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mapIntent)
        }

    }

    private fun initial() {
        img = findViewById(R.id.ticketDetail_img)
        movieName = findViewById(R.id.ticketDetail_movie)
        theaterName = findViewById(R.id.ticketDetail_theater)
        time = findViewById(R.id.ticketDetail_datetime)
        seat = findViewById(R.id.ticketDetail_seat)
        food = findViewById(R.id.ticketDetail_food)
        total = findViewById(R.id.ticketDetail_total)
        qrCode = findViewById(R.id.ticketDetail_qrcode)
        btnBack = findViewById(R.id.ticketDetail_back)
        mapbtn = findViewById(R.id.mapbtn)
    }
}