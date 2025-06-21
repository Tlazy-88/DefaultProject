package com.greatappmobile.myapplication.defaults

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import androidx.core.content.edit
import com.greatappmobile.myapplication.R

open class DefaultActivity : AppCompatActivity() {

    companion object {
        // default return e.g., "en", "vi", "fr", etc.
        private const val DEFAULT_LANGUAGE = "vi"
        private const val PREF_NAME = "settings"
        private const val LANGUAGE_KEY = "language"
    }

    override fun attachBaseContext(newBase: Context) {
        val languageCode = getSavedLocale(newBase)
        val context = applyLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }

    @Suppress("DEPRECATION")
    private fun applyLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            config.setLocales(LocaleList(locale))
        } else {
            config.setLocale(locale)
        }
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    private fun getSavedLocale(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun saveLocale(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        prefs.edit {
            putString(LANGUAGE_KEY, languageCode)
            apply()
        }
    }

    internal fun showLocaleDialog() {
        val languages = this.resources.getStringArray(R.array.language_names)
        val localeCodes = this.resources.getStringArray(R.array.language_codes)

        val currentLangCode = Locale.getDefault().language
        val selectedIndex = localeCodes.indexOf(currentLangCode).takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(this)
            .setTitle("Chọn ngôn ngữ")
            .setSingleChoiceItems(languages, selectedIndex) { dialog, which ->
                val selectedLangCode = localeCodes[which]
                saveLocale(this, selectedLangCode)
                recreate()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    internal fun setScreenOrientation(isPortrait: Boolean?= true){
        requestedOrientation= when(isPortrait){
            true -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            false -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    inline fun <reified T : Activity> Context.intentTo(
        vararg extras: Pair<String, Any?>,
        finishCurrent: Boolean = false
    ) {
        val intent = Intent(this, T::class.java)
        extras.forEach { (key, value) ->
            when (value) {
                is Int -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                is String -> intent.putExtra(key, value)
                is Float -> intent.putExtra(key, value)
                is java.io.Serializable -> intent.putExtra(key, value)

                null -> intent.putExtra(key, null as String?)
                else -> throw IllegalArgumentException("Unsupported type for key: $key")
            }
        }

        startActivity(intent)
        if (this is Activity && finishCurrent) finish()
    }

    internal fun toast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SSS", "On Create App Default Activity")
    }

    //end
}