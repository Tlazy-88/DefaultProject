package com.greatappmobile.myapplication.defaults

import android.content.Context
import androidx.core.content.edit

const val defaultSharedPreferencesName= "sharedPreferencesName"
const val defValue= "null"

fun <T> Context.getSharedPreference(key: String, type: Class<T>): T?{
    val sharedPreferencesObject = this.getSharedPreferences(defaultSharedPreferencesName, Context.MODE_PRIVATE)
    @Suppress("UNCHECKED_CAST")
    return when (type) {
        String::class.java -> sharedPreferencesObject.getString(key, defValue) as T
        Boolean::class.java -> java.lang.Boolean.valueOf(sharedPreferencesObject.getBoolean(key,false)) as T
        Float::class.java -> java.lang.Float.valueOf(sharedPreferencesObject.getFloat(key, 0f)) as T
        Int::class.java -> Integer.valueOf(sharedPreferencesObject.getInt(key, 0)) as T
        Long::class.java -> java.lang.Long.valueOf(sharedPreferencesObject.getLong(key, 0)) as T
        else -> null
    }
}

fun <T> Context.setSharedPreference(key: String, value: T){
    this.getSharedPreferences(defaultSharedPreferencesName, Context.MODE_PRIVATE).edit {
        when (value) {
            is String -> putString(key, value as String)
            is Boolean -> putBoolean(key, value as Boolean)
            is Float -> putFloat(key, value as Float)
            is Int -> putInt(key, value as Int)
            is Long -> putLong(key, value as Long)
            else -> return
        }
    }
}