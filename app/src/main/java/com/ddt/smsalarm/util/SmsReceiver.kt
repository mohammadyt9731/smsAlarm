package com.ddt.smsalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.gsm.SmsMessage
import androidx.annotation.CallSuper
import com.ddt.smsalarm.data.db.FilterDao
import com.ddt.smsalarm.ui.AlarmActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent?) {
    }
}

@AndroidEntryPoint
class SmsReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var dao: FilterDao

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val bundle = intent?.extras
            val msg: Array<SmsMessage?>
            if (bundle != null) {
                try {
                    val mPdus = bundle["pdus"] as Array<*>?
                    msg = arrayOfNulls(mPdus!!.size)
                    for (i in mPdus.indices) {
                        msg[i] = SmsMessage.createFromPdu(mPdus[i] as ByteArray)
                        val messageBody = msg[i]?.messageBody ?: ""
                        if (checkValidMessage(messageBody))
                            startAlarmActivity(context, messageBody)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkValidMessage(messageBody: String): Boolean {
        dao.getFiltersMessage().forEach {
            if (messageBody.contains(it))
                return true
        }
        return false
    }

    private fun startAlarmActivity(context: Context, messageBody: String) {
        val alarmActivityIntent =
            Intent(context, AlarmActivity::class.java)
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        alarmActivityIntent.putExtra(Constants.MESSAGE_KEY, messageBody)
        context.startActivity(alarmActivityIntent)
    }
}