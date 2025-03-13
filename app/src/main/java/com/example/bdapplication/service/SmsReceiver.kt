package com.example.bdapplication.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras != null) {
            val bundle: Bundle = intent.extras!!
            val pdus = bundle["pdus"] as? Array<*>
            val format = bundle.getString("format") // Get SMS format (GSM/CDMA)

            pdus?.forEach {
                val sms = SmsMessage.createFromPdu(it as ByteArray, format)
                val sender = sms.displayOriginatingAddress
                val message = sms.displayMessageBody
                Toast.makeText(context, "SMS from $sender: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
