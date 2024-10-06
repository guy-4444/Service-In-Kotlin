package com.guyi.serviceinkotlin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.guyi.serviceinkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mainFABPlay.setOnClickListener {
            startSoundService()
        }


        // Set up the BroadcastReceiver
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    val progress = intent.getIntExtra("PROGRESS", 0)
                    binding.mainPRGDownload.progress = progress
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter("com.example.UPDATE_PROGRESS"))

        // Button click opens a new activity
        binding.mainBTNNext.setOnClickListener {
            val newActivityIntent = Intent(this, SecondActivity::class.java)
            startActivity(newActivityIntent)
        }
    }

    private fun startSoundService() {
        val serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}