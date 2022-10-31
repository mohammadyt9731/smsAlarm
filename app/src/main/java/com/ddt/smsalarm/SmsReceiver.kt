package com.ddt.smsalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.gsm.SmsMessage


class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val bundle = intent.extras
            val msg: Array<SmsMessage?>
            if (bundle != null) {
                try {
                    val mPdus = bundle["pdus"] as Array<*>?
                    msg = arrayOfNulls(mPdus!!.size)
                    for (i in mPdus.indices) {
                        msg[i] = SmsMessage.createFromPdu(mPdus[i] as ByteArray)
                        val messageBody = msg[i]?.messageBody ?: ""
                        if (context != null) {
                            checkPhoneNumber(context, messageBody)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkPhoneNumber(context: Context, messageBody: String) {
        if (messageBody.contains("ایران ویزامتریک")
            || messageBody.contains("وقت مصاحبه")) {

            val alarmActivityIntent =
                Intent(context, AlarmActivity::class.java)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            alarmActivityIntent.putExtra("message", messageBody)
            context.startActivity(alarmActivityIntent)
        }
    }
}