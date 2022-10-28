package com.ddt.smsalarm

import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ddt.smsalarm.databinding.ActivityAlarmBinding


class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)


        addRequiredFlags()
        setMaxVolume()
        init()
        setOnClickListener()
        vibrate()
    }

    private fun addRequiredFlags() {

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    private fun setMaxVolume() {
        try {
            val audioManger = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManger.setStreamVolume(
                AudioManager.STREAM_RING,
                audioManger.getStreamMaxVolume(AudioManager.STREAM_RING),
                0
            )
        } catch (e: Exception) {
            //error
        }
    }

    private fun init() {
        val message = intent.extras?.getString("message") ?: ""
        if (message.isNotBlank())
            binding.tvSmaMessage.text = message

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(
            this,
            notification
        )
    }

    private fun setOnClickListener() {
        binding.btnClose.setOnClickListener {
            vibrator?.cancel()
            finish()
        }
    }

    private fun vibrate() {
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        30000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                it.vibrate(30000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ringtone?.play()
    }

    override fun onStop() {
        super.onStop()
        ringtone?.stop()
    }
}