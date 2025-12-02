package com.application.material.bookmarkswallet.app.features.bookmarkList.saveable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import timber.log.Timber

fun <T> MapSaver(onStoreCallback: (Map<FilterHp, T>) -> Unit) =
    listSaver<Map<FilterHp, T>, Pair<FilterHp, T>>(
        save = { map ->
            Timber.e("-----> SAVE " + map.onEach { "${it.key} - ${it.value}" })
            //store callback
            onStoreCallback.invoke(map)
            map.toList()
        },
        restore = { list ->
            Timber.e("-----> RESTORE + ${list.joinToString(",")}")
            list.toMutableList()
                .associate { it.first to it.second }
                .toMap()
        }
    )

fun <T> ListSaver(onStoreCallback: (List<T>) -> Unit) = listSaver<List<T>, T>(
    save = { list ->
        Timber.e("-----> SAVE " + list.joinToString(","))
        onStoreCallback.invoke(list)
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
    onStore: (Map<FilterHp, T>) -> Unit
) = rememberSaveable(
    inputs = inputs,
    init = init,
    stateSaver = MapSaver(
        onStoreCallback = onStore
    )
)

@Composable
fun <T> rememberSaveableList(
    vararg inputs: Any?,
    init: () -> MutableState<List<T>>,
    onStore: (List<T>) -> Unit,
) = rememberSaveable(
    inputs = inputs,
    init = init,
    stateSaver = ListSaver(
        onStoreCallback = onStore
    )
)
