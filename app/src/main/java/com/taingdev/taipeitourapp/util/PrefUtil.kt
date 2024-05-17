package com.taingdev.taipeitourapp.util

import android.content.SharedPreferences
import javax.inject.Inject

class PrefUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getLanguage(): String {
        return sharedPreferences.getString(PREF_LANG, DEFAULT_LANG) ?: DEFAULT_LANG
    }

    fun saveLanguage(language: String) {
        sharedPreferences.edit().putString(PREF_LANG, language).apply()
    }
}