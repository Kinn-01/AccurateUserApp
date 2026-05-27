package com.example.accurateuserapp.util

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPhoneNumber(): Boolean {
    return this.isNotBlank() && this.length in 8..15 && this.all { it.isDigit() }
}