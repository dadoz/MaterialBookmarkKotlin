package com.application.dev.david.materialbookmarkkot.modules.bookmarkList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark

class BookmarkListAdapter(private val items: List<Bookmark>) : RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflatedView = parent.inflate(R.layout.bookmark_view_item, false)
        return BookmarkViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bookmarkTitle.text = items[position].title
    }

    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var bookmarkTitle: TextView
        var bookmarkUrl: TextView

        init {
            itemView.setOnClickListener(this)
            bookmarkTitle = itemView.findViewById(R.id.bookmarkTitleTextViewId)
            bookmarkUrl = itemView.findViewById(R.id.bookmarkTitleTextViewId)
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

    }

}

/***
 * add inflate function on ViewGroup item
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
