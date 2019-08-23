package com.application.dev.david.materialbookmarkkot.modules.addBookmark

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.models.Bookmark
import com.application.dev.david.materialbookmarkkot.viewModels.AddBookmarkViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Action
import khronos.Dates
import kotlinx.android.synthetic.main.fragment_add_bookmark.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initView()
    }

    /**
     *
     */
    private fun initView() {
        val addBookmarkViewModel = ViewModelProviders.of(this).get(AddBookmarkViewModel::class.java)

        (mbNewBookmarkUrlEditTextId as EditText).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textChanged: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addBookmarkViewModel.findBookmarkInfoByUrl(textChanged.toString())
            }
        })

        addBookmarkViewModel.bookmarkInfoLiveData.observe(this, Observer{ bookmark ->
            Log.e(javaClass.name, "blalllala " + bookmark.title)
        })

        mbBookmarkSaveNewButtonId.setOnClickListener {
            val siteName = ""
            val title = mbNewBookmarkTitleEditTextId.text.toString()
            val iconPath = ""
            val id = UUID.randomUUID().toString()
            val url = mbNewBookmarkUrlEditTextId.text.toString()
            val newBookmark = Bookmark(siteName,title, iconPath, id, url, Dates.today)
            //saving cbs
            addBookmarkViewModel.saveBookmark(newBookmark)
            addBookmarkViewModel.saveBookmarkStatus.observe(this, Observer { status ->
                //success cb :)
                if (status) {
                    findNavController().popBackStack()
                    return@Observer
                }
                //error cb
                Snackbar.make(mbNewBookmarkMainViewId,
                    "error on saving bla bla bla", Snackbar.LENGTH_SHORT).show()
            })
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
