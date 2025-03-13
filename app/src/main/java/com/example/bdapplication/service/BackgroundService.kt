package com.example.bdapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.math.log

class BackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroundService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            for (i in 1..5){
                Log.d("BackgroundService", "onStartCommand: Running Operation $i")
                Thread.sleep(1000)
            }
            stopSelf()
        }.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        Log.d("BackgroundService", "Service Destroyed" )
    }
}