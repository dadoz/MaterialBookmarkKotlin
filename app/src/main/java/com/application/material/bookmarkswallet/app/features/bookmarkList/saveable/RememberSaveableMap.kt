package com.application.material.bookmarkswallet.app.features.bookmarkList.saveable

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.application.material.bookmarkswallet.app.features.bookmarkList.model.FilterHp
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import timber.log.Timber

const val MAP_KEY_VALUE_PARCEL_KEY = "MAP_KEY_VALUE_PARCEL_KEY"

class PairParceler<K>() : Parceler<Pair<Int, K>> {
    override fun create(parcel: Parcel): Pair<Int, K> =
        Pair(parcel.readInt(), parcel.readString() as K)

    override fun Pair<Int, K>.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(first)
        when (second) {
            is Boolean -> {
                parcel.writeInt(second as Int)
            }

            is Int -> {
                parcel.writeInt(second as Int)
            }

            is String -> {
                parcel.writeString(second as String)
            }
        }
    }
}

@Parcelize
@TypeParceler<Int, PairParceler<Boolean>>()
class IntKeyValueMap(val value: HashMap<Int, Boolean>) : Parcelable

@Composable
fun <K : FilterHp, J : Boolean, L : SnapshotStateMap<K, J>> rememberSaveableMapOld(
    vararg inputs: Any?,
    init: () -> L
): SnapshotStateMap<K, J> = rememberSaveable(
    inputs = inputs,
    init = init,
    saver = Saver(
        save = {
            Timber.e("-----> SAVE" + it.onEach { "${it.key} - ${it.value}" })
            Bundle()
                .also { bundle ->
                    bundle.putParcelable(
                        MAP_KEY_VALUE_PARCEL_KEY, IntKeyValueMap(
                            value = HashMap(
                                it.map { entry ->
                                    entry.key.ordinal to entry.value
                                }
                                    .toMap()
                            )
                        )
                    )
                }
        },
        restore = {
            Timber.e("-----> RESTORE + ${it}")
            it.getParcelable(
                MAP_KEY_VALUE_PARCEL_KEY,
            )
        }
    )
)

@Composable
fun <K : FilterHp, J : Boolean, L : SnapshotStateMap<K, J>> rememberSaveableMap2(
    vararg inputs: Any?,
    init: () -> L
): SnapshotStateMap<K, J> = rememberSaveable(
    saver = Saver(
        save = {
//                val originalValue = it.value.first()
//                bookmarkViewModel?.setSelectedFilterListType(
//                    value = originalValue
//                )
            Bundle().also {
//                    it.putString("value", originalValue.name)
            }
        },
        restore = {
            mutableStateMapOf(
                FilterHp.PINNED to false,
                FilterHp.SORT_BY_DATE to false
            ) as L

//                mutableStateOf(
//                    value = filterDefaultListType
//                ).also {
//                    coroutineScope.launch {
//                        it.value = bookmarkViewModel?.selectedFilterListTypeByStorage
//                            ?.first() ?: filterDefaultListType
//                    }
//                }
        }
    ),
    init = {
        mutableStateMapOf(
            FilterHp.PINNED to false,
            FilterHp.SORT_BY_DATE to false
        ) as L
    }
)


@Composable
fun <K : FilterHp, J : Boolean, L : SnapshotStateMap<K, J>> rememberSaveableMap1(
    vararg inputs: Any?,
    init: () -> L
): SnapshotStateMap<K, J> = rememberSaveable(
    saver = Saver(
        save = {
            Bundle()
                .also { bundle ->
                    bundle.putIntegerArrayList("value", ArrayList())
                    bundle.putIntegerArrayList("value", ArrayList())
                }
        },
        restore = {
            mutableStateMapOf(
                FilterHp.PINNED to false,
                FilterHp.SORT_BY_DATE to false
            ) as L
        }
    ),
    init = {
        mutableStateMapOf(
            FilterHp.PINNED to false,
            FilterHp.SORT_BY_DATE to false
        ) as L
    }
)

@Composable
fun <K : FilterHp, J : Boolean, L : SnapshotStateMap<K, J>> rememberSaveableMap(
    vararg inputs: Any,
    init: () -> L
) = rememberSaveable(
    inputs = inputs,
    init = init,
    saver = Saver<MutableMap<K, J>, List<Pair<K, J>>>(
        save = { map ->
            Timber.e("-----> SAVE" + map.onEach { "${it.key} - ${it.value}" })
            // Convert Map → List<Pair<String,String>>
            map.toList()
        },
        restore = { list ->
            // Convert back → Map
            Timber.e("-----> RESTORE + ${list.joinToString(",")}")
            list.toMap() as MutableMap<K, J>
        }
    )
)