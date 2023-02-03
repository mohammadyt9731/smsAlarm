package com.ddt.smsalarm.ui

import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ddt.smsalarm.data.model.Setting
import com.ddt.smsalarm.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null

    private val viewModel: MainViewModel by viewModels()
    private lateinit var setting: Setting


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)


        addRequiredFlags()
        init()
        setUpSetting()
        setOnClickListener()
        setTimerForClose()
    }

    private fun addRequiredFlags() {

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    private fun init() {
        runBlocking {
            setting = viewModel.getSetting()
        }
        val message = intent.extras?.getString("message") ?: ""
        if (message.isNotBlank())
            binding.tvSmaMessage.text = message

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(this, notification)

    }

    private fun setUpSetting() {
        if (setting.isMaxVolumeEnable) {
            setMaxVolume()
        }
        if (setting.isVibratorEnable) {
            vibrate()
        }
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
            Log.i("Mohammad", "setMaxVolume: ")
        }
    }

    private fun setOnClickListener() {
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun vibrate() {
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        setting.alarmDurationPerMinute.toLong(),
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                it.vibrate(setting.alarmDurationPerMinute.toLong())
            }
        }
    }

    private fun setTimerForClose() {
        lifecycleScope.launchWhenCreated {
            delay(setting.alarmDurationPerMinute * 60 * 1000L)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        ringtone?.play()
    }

    override fun onDestroy() {
        ringtone?.stop()
        vibrator?.cancel()
        super.onDestroy()
    }
}