package com.greatappmobile.myapplication.defaults

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit

private const val PREFS_NAME = "theme_prefs"
private const val KEY_THEME = "theme_mode"
const val THEME_LIGHT = 0
const val THEME_DARK = 1
const val THEME_SYSTEM = 2

private fun Context.prefs(): SharedPreferences =
    this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

fun Context.saveTheme(themeMode: Int) {
    prefs().edit { putInt(KEY_THEME, themeMode) }
    applyTheme(themeMode)
}

fun Context.getAppPrefsTheme(): Int {
    return prefs().getInt(KEY_THEME, THEME_SYSTEM)
}

fun applyTheme(themeMode: Int= THEME_SYSTEM) {
    when (themeMode) {
        THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}


fun Context.isDarkModeAppPrefs(): Boolean {
    return when (getAppPrefsTheme()) {
        THEME_DARK -> true
        THEME_LIGHT -> false
        else -> {
            val nightModeFlags = this.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        }
    }
}


fun Context.isDarkTheme(): Boolean {
    val nightModeFlags = resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}


fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return if (typedValue.resourceId != 0) {
        ContextCompat.getColor(this, typedValue.resourceId)
    } else {
        typedValue.data
    }
}

fun Int.toHexString(): String {
    return String.format("#%06X", 0xFFFFFF and this) // Dùng %06X để đảm bảo có 6 chữ số
}