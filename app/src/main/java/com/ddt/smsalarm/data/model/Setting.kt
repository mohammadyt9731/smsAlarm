package com.ddt.smsalarm.data.model

data class Setting(
    val isVibratorEnable: Boolean = true,
    val isMaxVolumeEnable: Boolean = true,
    val alarmDurationPerMinute: Int = 5
)
