package com.example.keyguard.security

import android.content.Context
import android.util.Base64
import java.security.MessageDigest

object PinManager {
    private const val PREF = "keyguard_pin_prefs"
    private const val KEY = "pin_hash"

    fun setPin(context: Context, pin: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY, sha256(pin)).apply()
    }

    fun hasPin(context: Context): Boolean =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).contains(KEY)

    fun verify(context: Context, pin: String): Boolean {
        val saved = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY, null)
        return saved != null && saved == sha256(pin)
    }

    private fun sha256(text: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(text.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
