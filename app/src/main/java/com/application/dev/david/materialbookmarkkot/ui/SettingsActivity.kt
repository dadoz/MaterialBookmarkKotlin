package com.application.dev.david.materialbookmarkkot.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.dev.david.materialbookmarkkot.BuildConfig
import com.application.dev.david.materialbookmarkkot.OnFragmentInteractionListener
import com.application.dev.david.materialbookmarkkot.R
import com.application.dev.david.materialbookmarkkot.modules.bookmarkList.inflate
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_add_bookmark.*

class SettingsActivity: AppCompatActivity(), OnFragmentInteractionListener, LifecycleOwner {
    private val items: List<Pair<String, String>> = listOf(
        Pair("Rate on PlayStore", ""),
        Pair("Contact us", ""),
        Pair("App Version", BuildConfig.VERSION_NAME)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initActionBar()
        initView()
    }

    private fun initActionBar() {
        setSupportActionBar(mbToolbarId)
        mbSettingsToolbarId.changeToolbarFont()
//        mbSettingsToolbarId.title = getString(R.string.settings_actionbar_string)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initView() {
        mbSettingsRecyeclerViewId.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SettingsAdapter(items)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    class SettingsAdapter(private val items: List<Pair<String, String>>): RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
            return SettingsViewHolder(parent.inflate(R.layout.setting_item))
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
            items[position].let {
                holder.settingLabel.text = it.first
                holder.settingValue.text = it.second
            }
        }

        class SettingsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val settingValue: TextView = view.findViewById(R.id.mbSettingsItemValueTextId)
            val settingLabel: TextView = view.findViewById(R.id.mbSettingsItemLabelTextId)
        }
    }
}
