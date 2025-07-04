package com.greatappmobile.myapplication.defaults

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.greatappmobile.myapplication.R
import com.greatappmobile.myapplication.dialogs.AppDialogBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    private fun createGradientDrawableBackground(
        colors: IntArray = intArrayOf(
            "#B6BFB4".toColorInt(),
            "#25C825".toColorInt()
        ),
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BOTTOM_TOP,
        topLeftRadius: Float = 24f,
        topRightRadius: Float = 24f,
        bottomRightRadius: Float = 24f,
        bottomLeftRadius: Float = 24f
    ): GradientDrawable {
        return GradientDrawable(orientation, colors).apply {

            //màu
            shape = GradientDrawable.RECTANGLE

            //corner
            cornerRadii = floatArrayOf(
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
            )

        }
    }

    internal fun showLocaleDialog() {
        val languages = this.resources.getStringArray(R.array.language_names)
        val localeCodes = this.resources.getStringArray(R.array.language_codes)

        val currentLangCode = Locale.getDefault().language
        val selectedIndex = localeCodes.indexOf(currentLangCode).takeIf { it >= 0 } ?: 0

        AppDialogBuilder(this)
            .setLayout(R.layout.dialog_locale_selector)
            .onBindView { view, dialog ->
                val rootView = view.findViewById<LinearLayout>(R.id.dialog_root)

                //set background
                val background = createGradientDrawableBackground()
                rootView.background = background


                val radioGroup = view.findViewById<RadioGroup>(R.id.lang_radio_group)
                val btnOk = view.findViewById<Button>(R.id.btn_ok)
                val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

                // Tạo RadioButton động cho từng ngôn ngữ
                languages.forEachIndexed { index, language ->
                    val radioButton = RadioButton(view.context).apply {
                        text = language
                        id = View.generateViewId()
                        isChecked = index == selectedIndex
                    }
                    radioGroup.addView(radioButton)
                }

                btnOk.setOnClickListener {
                    val checkedIndex = radioGroup.indexOfChild(
                        radioGroup.findViewById(radioGroup.checkedRadioButtonId)
                    ).takeIf { it >= 0 } ?: return@setOnClickListener

                    val selectedLangCode = localeCodes[checkedIndex]
                    saveLocale(this, selectedLangCode)
                    dialog.dismiss()
                    recreate()
                }

                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

            }
            .setCancelable(true)
            .setWidthPercent(0.9f)
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

    internal fun log(message: String, tag: String= "SSS"){
        Log.d(tag, message)
    }

    protected val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineError", "Caught $exception")
        exception.printStackTrace()
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(this@DefaultActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("On Create App Default Activity")
    }

    //end
}