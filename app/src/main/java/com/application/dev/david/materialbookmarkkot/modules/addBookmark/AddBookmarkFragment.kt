package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.ui.mbMaterialSearchView.MbMaterialSearchView
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Action
import khronos.Dates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.*
import kotlinx.android.synthetic.main.fragment_bookmark_list.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddBookmarkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddBookmarkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    val addBookmarkViewModel: AddBookmarkViewModel by lazy {
        ViewModelProviders.of(this).get(AddBookmarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search_bookmark -> {
                addBookmarkViewModel.updateWebviewByUrl((mbNewBookmarkUrlEditTextId as EditText).text.toString())
            }
        }
        return super.onOptionsItemSelected(item)

    }
    /**
     * init actionbar
     */
    private fun initActionBar() {
        listener?.showActionBarView("Search new Bookmark")
    }

    /**
     *
     */
    private fun initView() {

        addBookmarkViewModel.bookmarkSearchedUrlLiveData.observe(this, Observer{ searchedUrl ->
            mbBookmarkSearchedUrlWebViewId.loadUrl(searchedUrl)
            mbBookmarkSearchedUrPlaceholderId.visibility = GONE
            Log.e(javaClass.name, "blalllala " + searchedUrl)
        })

        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{ bookmarkInfo ->
            mbNewBookmarkTitleEditTextId.setText(bookmarkInfo.title)

            Glide.with(mbNewBookmarkIconImageViewId.context)
                .load(bookmarkInfo.icons[0].href)
                .apply(RequestOptions.circleCropTransform())
                .into(mbNewBookmarkIconImageViewId)
        })

        addBookmarkViewModel.saveBookmarkStatus.observe(this, Observer { status ->
            when (status) {
                //success cb :)
//                    true -> findNavController().popBackStack()
                true -> Snackbar.make(mbNewBookmarkMainViewId,
                    "SUCCESS", Snackbar.LENGTH_SHORT).show()
                //error cb :)
                false -> Snackbar.make(mbNewBookmarkMainViewId,
                    "error on saving bla bla bla", Snackbar.LENGTH_SHORT).show()
            }
        })

        mbBookmarkSaveNewButtonId.setOnClickListener {
            mbNewBookmarkIconLayoutId.visibility = VISIBLE
            addBookmarkViewModel.findBookmarkInfoByUrlAndSave(mbNewBookmarkUrlEditTextId.text.toString())
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
