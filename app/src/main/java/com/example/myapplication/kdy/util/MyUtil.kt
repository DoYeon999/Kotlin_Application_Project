package com.example.myapplication.kdy.util

import com.example.myapplication.kdy.APIApplication
import java.text.SimpleDateFormat
import java.util.Date

fun dateToString(date: Date): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}
