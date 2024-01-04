package com.example.movie_ticket_booking.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.movie_ticket_booking.Helper.AppInfo
import com.example.movie_ticket_booking.Helper.CreateOrder
import com.example.movie_ticket_booking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var theaterName:TextView
    private lateinit var date:TextView
    private lateinit var showtime:TextView
    private lateinit var seat:TextView
    private lateinit var food:TextView
    private lateinit var total:TextView
    private lateinit var img:ImageView
    private lateinit var btnBack:ImageButton
    private lateinit var btnPay:Button
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

//    private val appId = 2554
//    private val appKey = "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)


        initView()
        btnBack.setOnClickListener {
            finish()
        }


        val movie = AppData.selectedMovie
        val theater = AppData.selectedTheater
        val sellectedDate = AppData.selectedDate
        val selectedShowtime = AppData.selectedShowtime
        val seats = intent.getStringArrayListExtra("seatList")
        val foods = intent.getStringArrayListExtra("foodList")
        val totalPut = intent.getLongExtra("total", 0L)
        Picasso.get().load(movie?.img).into(img)
        name.text = movie?.name
        theaterName.text = "CGV "+theater?.theaterName
        date.text = sellectedDate
        showtime.text = selectedShowtime?.showtime
        if (foods?.isNotEmpty() == true){
            food.text = foods?.joinToString(", ")
        }else{
            food.visibility = View.GONE
        }
        seat.text = "Seats: " + seats?.joinToString(", ")
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        total.text = "Total: "+formatter.format(totalPut).toString() +"đ"



        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX)


        btnPay.setOnClickListener{

            val orderApi = CreateOrder()

            try {
                val data: JSONObject = orderApi.createOrder(totalPut.toString())
                val code = data.getString("returncode")
                if (code == "1") {

                    val token = data.getString("zptranstoken")
                    ZaloPaySDK.getInstance().payOrder(
                        this,
                        token,
                        "demozpdk://app",
                        object : PayOrderListener {

                            override fun onPaymentSucceeded(
                                transactionId: String,
                                transToken: String,
                                appTransID: String
                            ) {
                                Toast.makeText(
                                    this@PaymentActivity,
                                    "Thanh toán thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                    //val data = "${firebaseAuth.currentUser?.uid}|${movie?.name}|${theater?.theaterName}|$"
                                    //genQrCode(data)

                                val data = hashMapOf(
                                    "img" to "${movie?.img}",
                                    "movie_name" to "${movie?.name}",
                                    "seat" to seats,
                                    "food" to "${foods?.joinToString(", ")}",
                                    "theater_name" to "${theater?.theaterName}",
                                    "date" to "$sellectedDate",
                                    "showtime" to "${selectedShowtime?.showtime}",
                                    "total" to "$totalPut",
                                    "user_id" to "${firebaseAuth.currentUser?.uid}"

                                )
                                db.collection("tickets").add(data)
                                    .addOnCompleteListener {
                                        val intent = Intent(this@PaymentActivity, TicketActivity::class.java)
                                        finish()
                                        startActivity(intent)


                                    }

                            }

                            override fun onPaymentCanceled(
                                zpTransToken: String,
                                appTransID: String
                            ) {

                                Toast.makeText(
                                    this@PaymentActivity,
                                    "Thanh toán bị hủy",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onPaymentError(
                                zaloPayError: ZaloPayError,
                                zpTransToken: String,
                                appTransID: String
                            ) {

                                Toast.makeText(
                                    this@PaymentActivity,
                                    "Thanh toán thất bại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun initView() {
        img = findViewById(R.id.payment_img)
        name = findViewById(R.id.payment_tv_tenphim)
        theaterName = findViewById(R.id.payment_tv_theater)
        date = findViewById(R.id.payment_tv_time)
        showtime = findViewById(R.id.payment_tv_hour)
        food = findViewById(R.id.payment_tv_food)
        seat = findViewById(R.id.payment_tv_ghe)
        total = findViewById(R.id.payment_tv_total)
        btnBack = findViewById(R.id.btn_payment_back)
        btnPay = findViewById(R.id.payment_button)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

    private fun genQrCode(data: String){
        try {
            // Tạo mã QR code từ dữ liệu
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 120, 120)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

//            // Hiển thị mã QR code trong ImageView
//            imageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}