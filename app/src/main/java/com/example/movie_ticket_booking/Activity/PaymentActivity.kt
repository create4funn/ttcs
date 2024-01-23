package com.example.movie_ticket_booking.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.movie_ticket_booking.Helper.AppInfo
import com.example.movie_ticket_booking.Helper.CreateOrder
import com.example.movie_ticket_booking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentActivity : AppCompatActivity() {
    private lateinit var name:TextView
    private lateinit var countDown:TextView
    private lateinit var theaterName:TextView
    private lateinit var date:TextView
    private lateinit var showtime:TextView
    private lateinit var seat:TextView
    private lateinit var food:TextView
    private lateinit var total:TextView
    private lateinit var img:ImageView
    private lateinit var btnBack:ImageButton
    private lateinit var btnPay:Button
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var db1 : FirebaseFirestore
    private lateinit var db2 : FirebaseFirestore
    private lateinit var countDownTimer: CountDownTimer
//    private val broadcastService = BroadcastService()
private var isCountdownPaused = false
    private var remainingTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())




//    private val appId = 2554
//    private val appKey = "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)

        db1 = FirebaseFirestore.getInstance()
        db2 = FirebaseFirestore.getInstance()
        initView()

        val movie = AppData.selectedMovie
        val theater = AppData.selectedTheater
        val sellectedDate = AppData.selectedDate
        val selectedShowtime = AppData.selectedShowtime
        val seats = intent.getStringArrayListExtra("seatList")
        val foods = intent.getStringArrayListExtra("foodList")
        val totalPut = intent.getLongExtra("total", 0L)
        btnBack.setOnClickListener {
            deleteState(movie?.name!!, theater?.theaterName!!, sellectedDate!!, selectedShowtime?.showtime!!, seats!!)

        }



        val check = intent.getBooleanExtra("check", true)
        Log.d("bvb", "$check")

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

        if (seats == null){
            finish()
        }
//        startCountdown(60000L)
//        val intent = Intent(this, BroadcastService::class.java)
//        startService(intent)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX)


        btnPay.setOnClickListener{

            if (checkTickets(movie?.name!!,theater?.theaterName!!,sellectedDate!!,selectedShowtime?.showtime!!,seats!!)){
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

                                    deleteState(movie?.name!!, theater?.theaterName!!, sellectedDate!!, selectedShowtime?.showtime!!, seats!!)

                                    val data1 = hashMapOf(
                                        "img" to "${movie?.img}",
                                        "movie_name" to "${movie?.name}",
                                        "seat" to seats,
                                        "food" to "${foods?.joinToString(", ")}",
                                        "theater_name" to "${theater?.theaterName}",
                                        "date" to "$sellectedDate",
                                        "showtime" to "${selectedShowtime?.showtime}",
                                        "total" to "$totalPut",
//                                    "user_id" to "${userID}"

                                    )
                                    val data2 = hashMapOf(
                                        "img" to "${movie?.img}",
                                        "movie_name" to "${movie?.name}",
                                        "seat" to seats,
                                        "food" to "${foods?.joinToString(", ")}",
                                        "theater_name" to "${theater?.theaterName}",
                                        "date" to "$sellectedDate",
                                        "showtime" to "${selectedShowtime?.showtime}",
                                        "total" to "$totalPut",
                                        "user_id" to userID

                                    )

                                    db1.collection("ticket").add(data2)

                                    db2.collection("Users").document(userID).collection("ticket")
                                        .add(data1).addOnCompleteListener {

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
            }else{
                Toast.makeText(this,"The seat has just been booked by someone else, please choose another seat.", Toast.LENGTH_LONG).show()
            }

        }

    }

//    private val broadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            // Update GUI
//            updateGUI(intent)
//        }
//    }
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
        countDown = findViewById(R.id.tv_countdown)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

    private fun deleteState(movie: String, theater: String, date: String, showtime: String, seat: kotlin.collections.ArrayList<String> ){

        val collectionRef = db1.collection("state").document(theater)
            .collection(convertDateFormat(date))

// Xây dựng truy vấn với các điều kiện
        val query = collectionRef
            .whereEqualTo("showtime", showtime)
            .whereEqualTo("movie_name", movie)
            .whereArrayContainsAny("seat", seat)

// Thực hiện truy vấn
        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Duyệt qua từng tài liệu hợp lệ và xóa chúng
                for (document in querySnapshot.documents) {
                    val documentRef = collectionRef.document(document.id)
                    documentRef.delete()
                        .addOnSuccessListener {
                            finish()
                        }

                }
            }



        Log.d("bvb", "test")
    }
    fun checkTickets(movie:String, theater:String, date:String, showtime:String, seats: List<String> ): Boolean {
        var check = true
        db1.collection("ticket")
            .whereEqualTo("movie_name", movie)
            .whereEqualTo("theater_name", theater)
            .whereEqualTo("date", date)
            .whereEqualTo("showtime", showtime)
            .whereArrayContainsAny("seat",seats)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    check = false
                }
            }
        return check
    }

    fun convertDateFormat(inputDate: String): String {
        try {
            // Định dạng đầu vào
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Định dạng đầu ra
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            // Chuyển đổi chuỗi ngày từ định dạng đầu vào sang định dạng đầu ra
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            // Xử lý nếu có lỗi, ví dụ: in ra log hoặc thông báo lỗi
            e.printStackTrace()
            return inputDate // Trả về ngày không thay đổi nếu có lỗi
        }
    }

//    override fun onResume() {
//        super.onResume()
//        registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.broadcastService.COUNTDOWN_BR))
//
//    }
//
////    override fun onPause() {
////        super.onPause()
//////        unregisterReceiver(broadcastReceiver)
////        registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.broadcastService.COUNTDOWN_BR))
////    }
////
////    override fun onStop() {
////        registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.broadcastService.COUNTDOWN_BR))
////
////        super.onStop()
////    }
////
////    override fun onDestroy() {
////        finish()
////        super.onDestroy()
////    }
//
//    private fun updateGUI(intent: Intent?) {
//        intent?.extras?.let {
//            val millisUntilFinished = it.getLong("countdown", 30000)
//            if (millisUntilFinished < 1000){
//                Toast.makeText(this, "Time out", Toast.LENGTH_LONG).show()
//                finish()
//                stopService(Intent(this, BroadcastService::class.java))
//            }
//
//            countDown.text = (millisUntilFinished / 1000).toString()
//
//            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
//            sharedPreferences.edit().putLong("time", millisUntilFinished).apply()
//        }
//    }



//    private fun startCountdown(milliseconds: Long) {
//        countDownTimer = object : CountDownTimer(milliseconds, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                // Cập nhật TextView với thời gian còn lại
//                countDown.text = "${millisUntilFinished / 1000}"
//                remainingTime = millisUntilFinished
//            }
//
//            override fun onFinish() {
//                // Đếm ngược kết thúc, thực hiện các hành động cần thiết
////                finish()
////                Toast.makeText(this@PaymentActivity, "time out", Toast.LENGTH_LONG).show()
//            }
//        }.start()

//        if (isCountdownPaused) {
//            countDownTimer.start()
//        } else {
//            countDownTimer.start()
//        }
 //   }

    override fun onPause() {
        super.onPause()
//        Log.d("mmm", "pause")
//
//
//        if (countDownTimer != null) {
//            countDownTimer.cancel()
//            isCountdownPaused = true
//        }


    }

    override fun onStop() {
        super.onStop()
//        Log.d("mmm", "stop")
//        if (countDownTimer != null) {
//            countDownTimer.cancel()
//            isCountdownPaused = true
//        }
//        handler.postDelayed({
//            // Kiểm tra xem activity có đang ở trạng thái "stop" hay không
//            if (isCountdownPaused) {
//                // Kết thúc activity
//                finish()
//            }
//        }, 120000)
    }

    override fun onResume() {
        super.onResume()

        // Khởi động lại bộ đếm ngược nếu đã được tạm dừng
//        if (isCountdownPaused) {
//            startCountdown(remainingTime)
//            isCountdownPaused = false
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

//        // Người dùng thoát khỏi ứng dụng, hủy bỏ đếm ngược
//        countDownTimer.cancel()
    }
}