package com.application.material.bookmarkswallet.app.features.bookmarkList.saveable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp

fun <T> MapSaver(onStoreCallback: (Map<FilterHp, T>) -> Unit) =
    listSaver<Map<FilterHp, T>, Pair<FilterHp, T>>(
        save = { map ->
            //store callback
            onStoreCallback.invoke(map)
            map.toList()
        },
        restore = { list ->
            list.toMutableList()
                .associate { it.first to it.second }
                .toMap()
        }
    )

fun <T> ListSaver(onStoreCallback: (List<T>) -> Unit) = listSaver<List<T>, T>(
    save = { list ->
        onStoreCallback.invoke(list)
        list
    },
    restore = { list ->
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

