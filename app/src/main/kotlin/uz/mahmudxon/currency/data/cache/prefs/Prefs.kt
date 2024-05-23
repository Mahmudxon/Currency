package uz.mahmudxon.currency.data.cache.prefs

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(context: Context) {
    private val prefsName: String = "Currency"
    private val prefs: SharedPreferences by lazy {
       context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }


    fun save(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun save(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun save(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun save(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getString(key: String) = prefs.getString(key, null)

    fun get(key: String, defValue: Int) = prefs.getInt(key, defValue)

    fun get(key: String, defValue: String) = prefs.getString(key, defValue) ?: ""

    fun get(key: String, defValue: Float) = prefs.getFloat(key, defValue)

    fun get(key: String, defValue: Boolean) = prefs.getBoolean(key, defValue)

    fun get(key: String, defValue: Long) = prefs.getLong(key, defValue)

    fun clear() {
        prefs.edit().clear().apply()
    }

}