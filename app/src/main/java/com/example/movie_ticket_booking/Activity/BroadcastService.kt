package com.example.movie_ticket_booking.Activity

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class BroadcastService() : Service() {

    val COUNTDOWN_BR = "com.example.movie_ticket_booking.Activity"
    val intent = Intent(COUNTDOWN_BR)
    var countDownTimer: CountDownTimer? = null

    companion object {
        val broadcastService = BroadcastService()
    }

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()


        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)

        var millis = sharedPreferences.getLong("time", 3000)
        if (millis / 1000 == 0L) {
            millis = 30000
        }

//        val millis = 30000L
        countDownTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                intent.putExtra("countdown", millisUntilFinished)
                Log.d("mmm", "millis: $millis, illisUntilFinished: $millisUntilFinished")
                sendBroadcast(intent)
            }

            override fun onFinish() {
                // Handle countdown finish, if needed
            }
        }
        Log.d("mmm","onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("mmm","onStartCommand")
        countDownTimer?.start()
        return START_NOT_STICKY
    }


    override fun onDestroy() {
//        countDownTimer?.cancel()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}