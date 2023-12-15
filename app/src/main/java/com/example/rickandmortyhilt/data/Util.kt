package com.example.rickandmortyhilt.data

import android.util.Log

fun Any.logError(message: String?) {
    message?.let {
        Log.d(this::class.java.name, it)
    }
}