package com.application.dev.david.materialbookmarkkot.data.local.db

import android.util.Log
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.application.dev.david.materialbookmarkkot.data.local.BookmarkDao
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import khronos.toDate


@Database(entities = [Bookmark::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao

    init {
        val list : ArrayList<Bookmark> = ArrayList()
        list.add(Bookmark("bll", "Instagram", "Image", "", "www.instagram.com", "2019-06-19".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Facebook", "Image", "", "www.facebook.com", "2019-05-01".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Google", "Image", "", "www.google.com", "2019-02-11".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Vodafone", "Image", "", "www.vodafone.com", "2019-04-22".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Outlook", "Image", "", "www.outlook.it", "2019-04-30".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Gmail", "Image", "", "www.gmail.com", "2019-05-18".toDate("yyyy-MM-dd")))
        list.add(Bookmark("bll", "Blablacar", "Image", "", "www.blablacar.com", "2019-05-12".toDate("yyyy-MM-dd")))
    }
}
