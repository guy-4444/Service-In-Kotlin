package com.guyi.serviceinkotlin

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MyService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper()) // Updated Handler initialization

    private var counter = 0

    // Runnable that plays sound and sends broadcast every 3 seconds
    private val playSoundRunnable = object : Runnable {
        override fun run() {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }

            // Send broadcast to update UI
            counter++
            sendBroadcastToActivities()



            if (counter < 10) {
                handler.postDelayed(this, 3000) // Repeat every 3 seconds
            }
        }
    }

    private fun sendBroadcastToActivities() {
        val broadcastIntent = Intent("com.example.UPDATE_PROGRESS")
        broadcastIntent.putExtra("PROGRESS", counter * 10)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.message_notification) // Replace with your sound file
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(playSoundRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(playSoundRunnable)
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
