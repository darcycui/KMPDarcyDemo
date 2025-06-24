package com.darcy.kmpdemo.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    private const val TIME_FORMATTER: String = "yyyy-MM-dd HH:mm:ss"

    fun getCurrentTimeStamp(): String {
        val formatter = DateTimeFormatter.ofPattern(TIME_FORMATTER)
        return LocalDateTime.now().format(formatter)
    }
}