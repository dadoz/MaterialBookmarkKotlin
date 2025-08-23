package com.application.material.bookmarkswallet.app.features.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.material.bookmarkswallet.app.BuildConfig
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.databinding.ActivitySettingsBinding
import com.application.material.bookmarkswallet.app.extensions.changeToolbarFont
import com.application.material.bookmarkswallet.app.features.bookmarkList.adapter.inflate
import com.application.material.bookmarkswallet.app.features.settings.SettingsActivity.SettingsAdapter.SettingsViewHolder
import dagger.hilt.android.AndroidEntryPoint

@Deprecated("not used")
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var binding: ActivitySettingsBinding
    private val items: List<Triple<Int, String, String>> = listOf(
        Triple(0, "Rate on PlayStore", ""),
        Triple(1, "Contact us", ""),
        Triple(2, "App Version", BuildConfig.VERSION_NAME)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySettingsBinding.inflate(layoutInflater)
            .also {
                binding = it
            }.root.also {
                setContentView(it)
                initActionBar()
                initView()
            }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mbSettingsToolbarId)
        binding.mbSettingsToolbarId.changeToolbarFont()
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() {
        binding.mbSettingsRecyeclerViewId.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SettingsAdapter(items) { item, _ ->
                when (item.first) {
                    0 -> rateUsOnPlaystore()
                    1 -> contactUs()
                }
            }
        }
    }

    private fun contactUs() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("tunnus.android@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Subject text here...")
            type = "text/html"
        })
    }

    private fun rateUsOnPlaystore() {
        val goToMarket = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=${packageName}")
        ).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
        }

        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${packageName}")
                )
            )
        }
    }

    class SettingsAdapter(
        private val items: List<Triple<Int, String, String>>,
        val callback: (item: Triple<Int, String, String>, position: Int) -> Unit
    ) : RecyclerView.Adapter<SettingsViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
            return SettingsViewHolder(parent.inflate(R.layout.setting_item))
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
            items[position].let { item ->
                holder.settingLabel.text = item.second
                holder.settingValue.text = item.third
                if (item.third.isEmpty()) {
                    holder.itemView.setOnClickListener {
                        callback.invoke(item, position)
                    }
                }
            }

        }

        class SettingsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val settingValue: TextView = view.findViewById(R.id.mbSettingsItemValueTextId)
            val settingLabel: TextView = view.findViewById(R.id.mbSettingsItemLabelTextId)
        }
    }
}
