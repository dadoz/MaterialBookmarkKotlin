package com.application.material.bookmarkswallet.app.features.bookmarkList.saveable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import timber.log.Timber

const val MAP_KEY_VALUE_PARCEL_KEY = "MAP_KEY_VALUE_PARCEL_KEY"

fun <T> MapSaver() = listSaver<Map<FilterHp, T>, Pair<FilterHp, T>>(
    save = { map ->
        Timber.e("-----> SAVE " + map.onEach { "${it.key} - ${it.value}" })
        map.toList()
    },
    restore = { list ->
        Timber.e("-----> RESTORE + ${list.joinToString(",")}")
        list.toMutableList()
            .associate { it.first to it.second }
            .toMap()
    }
)

fun <T> ListSaver() = listSaver<List<T>, T>(
    save = { list ->
        Timber.e("-----> SAVE " + list.joinToString(","))
        list
    },
    restore = { list ->
        Timber.e("-----> RESTORE + ${list.joinToString(",")}")
        list
    }
)

@Composable
fun <T> rememberSaveableMap(
    vararg inputs: Any?,
    init: () -> MutableState<Map<FilterHp, T>>,
) = rememberSaveable(
    inputs = inputs,
    init = init,
    stateSaver = MapSaver()
)

@Composable
fun <T> rememberSaveableList(
    vararg inputs: Any?,
    init: () -> MutableState<List<T>>,
) = rememberSaveable(
    inputs = inputs,
    init = init,
    stateSaver = ListSaver()
)
