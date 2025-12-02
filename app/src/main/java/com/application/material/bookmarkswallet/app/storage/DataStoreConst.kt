package com.application.material.bookmarkswallet.app.storage

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal const val MATERIAL_BOOKMARK_STORE_PREFERENCES = "MATERIAL_BOOKMARK_STORE_PREFERENCES"
internal val SELECTED_FILTER_LIST_TYPE = stringPreferencesKey("SELECTED_FILTER_LIST_TYPE")
internal val SELECTED_FILTER_HP_MAP_TYPE = stringSetPreferencesKey("SELECTED_FILTER_HP_MAP_TYPE")
