package com.application.material.bookmarkswallet.app.preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


import android.content.Context
import com.application.material.bookmarkswallet.app.preferences.PreferenceProperty.SharedPrefProvider

/**
 * @author krzysztof.kosobudzki
 */
class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<SharedPrefProvider, T> {

    override fun getValue(thisRef: SharedPrefProvider, property: KProperty<*>): T =
        thisRef.getPreferences()
            ?.getter(key, defaultValue)?: defaultValue

    override fun setValue(thisRef: SharedPrefProvider, property: KProperty<*>, value: T) =
        thisRef.getPreferences()
            ?.edit()
            ?.setter(key, value)
            ?.apply()?: Unit

    interface SharedPrefProvider {
        fun getPreferences() : SharedPreferences?
    }

    companion object {
        const val SHARED_PREF_NAME = "MATERIAL_BOOKMARK_SHARED_PREF_NAME"
    }

}

fun intPreference(key: String, defaultValue: Int = 0) : ReadWriteProperty<SharedPrefProvider, Int> =
        PreferenceProperty(
            key = key,
            defaultValue = defaultValue,
            getter = SharedPreferences::getInt,
            setter = SharedPreferences.Editor::putInt
        )

fun booleanPreference(key: String, defaultValue: Boolean = false) : ReadWriteProperty<SharedPrefProvider, Boolean> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getBoolean,
        setter = SharedPreferences.Editor::putBoolean
    )

fun stringPreference(key: String, defaultValue: String? = null) : ReadWriteProperty<SharedPrefProvider, String?> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
    )

fun Context.getPreferences(): SharedPreferences =
    getSharedPreferences(PreferenceProperty.SHARED_PREF_NAME, Context.MODE_PRIVATE)
