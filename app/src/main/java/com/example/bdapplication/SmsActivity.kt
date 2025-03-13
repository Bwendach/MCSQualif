package com.example.bdapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bdapplication.databinding.ActivitySmsBinding
import com.example.bdapplication.service.BackgroundService

class SmsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), 100)
        }

        binding.btnSend.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val message = binding.etMessage.text.toString()
            sendSMS(phoneNumber, message)
        }

        binding.btnStartService.setOnClickListener {
            val intent = Intent(this, BackgroundService::class.java)
            startService(intent)
        }

    }

    fun sendSMS(phoneNumber: String, message: String) {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 101)
        } else {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }
    }
}