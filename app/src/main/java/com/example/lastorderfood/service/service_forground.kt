package com.example.lastorderfood.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.lastorderfood.R
import io.github.muddz.styleabletoast.StyleableToast

class service_forground : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val service_id=1
            val channel_id="channel_id"
            val channel= NotificationChannel(channel_id,"Defualt", NotificationManager.IMPORTANCE_DEFAULT)
            val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            val notification= Notification.Builder(this,channel_id).apply {
                setContentTitle("Easy food")
                setContentText("App is activity")
                setSmallIcon(R.drawable.easyfood)
            }.build()

            startForeground(service_id,notification)
       }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showmessage()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showmessage() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
              //  Toast.makeText(applicationContext,"this is activity", Toast.LENGTH_SHORT).show()
                StyleableToast.makeText(applicationContext,"easy food is activity",R.style.exampleToast).show()
            },500

        )
    }
}