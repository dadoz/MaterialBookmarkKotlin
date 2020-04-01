package com.application.dev.david.materialbookmarkkot.preferences

import android.content.Context
import android.content.SharedPreferences

open class SharedPrefProv(val context: Context?) : PreferenceProperty.SharedPrefProvider {
    override fun getPreferences(): SharedPreferences? = context?.getPreferences()
}