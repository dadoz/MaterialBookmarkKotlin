package com.application.material.bookmarkswallet.app.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.BookmarkListType
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * Documentation of DataStore retrieved from
 * https://developer.android.com/topic/libraries/architecture/datastore
 * now we dont use anymore sharedpref but datastore
 */
@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    private val weakContext: WeakReference<Context> = WeakReference(context)

    private val Context.dataStorePreferences: DataStore<Preferences> by preferencesDataStore(
        name = MATERIAL_BOOKMARK_STORE_PREFERENCES,
        scope = CoroutineScope(
            context = Dispatchers.Default
        )
    )

    var selectedFilterListType: Flow<List<BookmarkListType>?>? = null
        get() = weakContext.get()
            ?.dataStorePreferences
            ?.data
            ?.map { preferences ->
                preferences[SELECTED_FILTER_LIST_TYPE]
                    ?.let {
                        listOf(
                            BookmarkListType.valueOf(it)
                        )
                    }
            }
        private set

    fun setSelectedFilterListType(
        value: String?,
        coroutineContext: CoroutineContext = Dispatchers.Main
    ) {
        value?.let {
            CoroutineScope(
                context = coroutineContext
            )
                .launch {
                    weakContext.get()
                        ?.dataStorePreferences
                        ?.edit { preferences ->
                            preferences[SELECTED_FILTER_LIST_TYPE] = value
                        }
                }
        }
    }

    var selectedFilterHpMap: Flow<Map<FilterHp, Boolean>?>? = null
        get() = weakContext.get()
            ?.dataStorePreferences
            ?.data
            ?.map { preferences ->
                preferences[SELECTED_FILTER_HP_MAP_TYPE]
                    ?.associate {
                        it.split(":")
                            .let { item ->
                                (FilterHp.entries.find { it.name == item[0] }
                                    ?: FilterHp.entries[0]) to item[1].toBoolean()
                            }
                    }
            }
        private set

    fun setSelectedFilterHpMap(
        filterHpMap: Map<FilterHp, Boolean>,
        coroutineContext: CoroutineContext = Dispatchers.Main
    ) {
        CoroutineScope(
            context = coroutineContext
        )
            .launch {
                weakContext.get()
                    ?.dataStorePreferences
                    ?.edit { preferences ->
                        preferences[SELECTED_FILTER_HP_MAP_TYPE] = filterHpMap
                            .map {
                                it.key.name + ":" + it.value
                            }
                            .toSet()
                    }
            }
    }

    suspend fun removeKey(userEmailKey: Preferences.Key<String>) {
        weakContext.get()?.dataStorePreferences?.edit {
            it.remove(key = userEmailKey)
        }
    }

    suspend fun clear() {
        weakContext.get()?.dataStorePreferences
            ?.edit {
                it.clear()
            }
    }
}

fun <T : Any> String.deserializeFromJson(objClass: Class<T>): T? =
    Moshi.Builder().build().adapter(objClass).fromJson(this)

fun <T : Any> serializeToJson(obj: T): String =
    Moshi.Builder().build().adapter(obj.javaClass).toJson(obj)
