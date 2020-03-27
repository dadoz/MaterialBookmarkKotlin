package com.application.dev.david.materialbookmarkkot.preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @author krzysztof.kosobudzki
 */
private const val APP_PREF_NAME = "APP_PREF_NAME"

private class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        thisRef.context!!.getPreferences()
            .getter(key, defaultValue)

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) =
        thisRef.context!!.getPreferences()
            .edit()
            .setter(key, value)
            .apply()

    private fun Context.getPreferences(): SharedPreferences =
        getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        public const val SHARED_PREF_NAME = "MATERIAL_BOOKMARK_SHARED_PREF_NAME"
    }

}

fun intPreference(key: String, defaultValue: Int = 0) : ReadWriteProperty<Fragment, Int> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getInt,
        setter = SharedPreferences.Editor::putInt
    )

fun booleanPreference(key: String, defaultValue: Boolean = false) : ReadWriteProperty<Fragment, Boolean> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getBoolean,
        setter = SharedPreferences.Editor::putBoolean
    )

fun stringPreference(key: String, defaultValue: String? = null) : ReadWriteProperty<Fragment, String?> =
    PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
    )