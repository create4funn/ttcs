package com.example.movie_ticket_booking.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.movie_ticket_booking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // No user is signed in
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

// authStateListener sẽ được kích hoạt khi có bất kỳ thay đổi nào trong trạng thái xác thực của người dùng
        //đăng ký một AuthStateListener với đối tượng FirebaseAuth
        auth.addAuthStateListener(authStateListener)

        Handler(Looper.getMainLooper()).postDelayed({
            // loại bỏ AuthStateListener khỏi FirebaseAuth sau thời gian delay
            auth.removeAuthStateListener(authStateListener)
        }, 4000)
    }
}