package com.ddt.smsalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.gsm.SmsMessage
import android.util.Log


class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                val bundle = intent.extras
                val msg: Array<SmsMessage?>
                var smsFrom: String
                if (bundle != null) {
                    try {
                        val mPdus = bundle["pdus"] as Array<*>?
                        msg = arrayOfNulls(mPdus!!.size)
                        for (i in mPdus.indices) {
                            msg[i] = SmsMessage.createFromPdu(mPdus[i] as ByteArray)
                            smsFrom = msg[i]?.getOriginatingAddress() ?: ""
                            val smsBody = msg[i]?.messageBody ?: ""
                            checkPhoneNumber(context, smsFrom, smsBody)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }

    private fun checkPhoneNumber(context: Context?, phoneNumber: String, message: String) {
        val sharedPreferences: SharedPreferences? =
            context?.getSharedPreferences("myShared", Context.MODE_PRIVATE)
        if (phoneNumber == sharedPreferences?.getString("phoneNumber", "empty")) {

            val alarmActivityIntent =
                Intent(context, AlarmActivity::class.java)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            alarmActivityIntent.putExtra("message", message)
            context.startActivity(alarmActivityIntent)
        }

    }
}