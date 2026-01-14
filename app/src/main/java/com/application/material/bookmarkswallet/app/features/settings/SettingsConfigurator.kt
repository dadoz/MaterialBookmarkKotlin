package com.application.material.bookmarkswallet.app.features.settings

import androidx.annotation.Keep
import com.application.material.bookmarkswallet.app.R
import com.application.material.bookmarkswallet.app.ui.style.MbColor
import timber.log.Timber


@Keep
enum class SettingsChildrenItemType {
    SETTING_CHILD_TEXT,
    SETTING_CHILD_SWITCH,
    SETTING_CHILD_COLOR
}

@Keep
enum class SettingsItemType {
    SETTING_USER,
    SETTING_BUY_ME_A_COFFEE,
    SETTING_INAPP_PAY_FEAT,
    SETTING_SKIN_COLOR,
    SETTING_SYNC_ON_GOOGLE,
    SETTING_CONTACT,
    SETTING_RATE_ON_PLAYSTORE,
    SETTING_APP_VERSION
}

interface ChildrenItem {
    val childrenItemType: SettingsChildrenItemType
}

@Keep
data class ChildrenTextItem(
    override val childrenItemType: SettingsChildrenItemType = SettingsChildrenItemType.SETTING_CHILD_TEXT,
    val label: Int
) : ChildrenItem

@Keep
data class ChildrenSwitchItem(
    override val childrenItemType: SettingsChildrenItemType = SettingsChildrenItemType.SETTING_CHILD_SWITCH,
    val labelRes: Int,
    val isChecked: Boolean = false,
) : ChildrenItem

@Keep
data class ChildrenColorItem(
    override val childrenItemType: SettingsChildrenItemType = SettingsChildrenItemType.SETTING_CHILD_COLOR,
    val label: Int,
    val colorList: List<androidx.compose.ui.graphics.Color>
) : ChildrenItem

@Keep
data class SettingsItem(
    val type: SettingsItemType,
    val iconRes: Int? = null,
    val nameRes: Int? = null,
    val childrenItem: List<ChildrenItem>? = null,
    val descriptionRes: Int? = null,
    val isProFeat: Boolean = false,
    val ctaLabel: Int? = null,
    val ctaAction: (() -> Unit)? = null
)

val settingsComponentItems: List<SettingsItem> = listOf(
    SettingsItem(
        type = SettingsItemType.SETTING_USER,
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_BUY_ME_A_COFFEE,
        nameRes = R.string.buy_me_a_coffee_label,
        descriptionRes = R.string.buy_me_a_coffee_descr,
        iconRes = R.drawable.ic_buymeacoffee_light,
        ctaLabel = R.string.buy_me_a_coffee_label,
        ctaAction = {
            Timber.e("buy me a coffeee")
        }
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_INAPP_PAY_FEAT,
        isProFeat = true,
        nameRes = R.string.in_app_pay_feat_label,
        childrenItem = listOf(
            ChildrenSwitchItem(
                labelRes = R.string.search_with_ai_label,
                isChecked = true
            ),
            ChildrenSwitchItem(
                labelRes = R.string.enable_preview_with_ecosia,
                isChecked = true
            ),
            ChildrenSwitchItem(
                labelRes = R.string.fine_search_with_ai,
                isChecked = true
            ),
            ChildrenSwitchItem(
                labelRes = R.string.download_bookmark,
                isChecked = true
            ),
            ChildrenSwitchItem(
                labelRes = R.string.inapp_purchase_config,
                isChecked = true
            ),
        )
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_SKIN_COLOR,
        isProFeat = true,
        nameRes = R.string.skin_color_label,
        childrenItem = listOf(
            ChildrenSwitchItem(
                labelRes = R.string.settings,
                isChecked = true
            ),
            ChildrenColorItem(
                label = R.string.settings,
                colorList = listOf(
                    MbColor.Yellow,
                    MbColor.RedVermilion,
                    MbColor.VioletLilla,
                    MbColor.PinkElectric,
                    MbColor.GreenRubin
                )
            )
        )
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_SYNC_ON_GOOGLE,
        nameRes = R.string.sync_on_google_label,
        childrenItem = listOf(
            ChildrenSwitchItem(
                labelRes = R.string.sync_google_enabled,
                isChecked = true
            ),
            ChildrenSwitchItem(
                labelRes = R.string.sync_bookmark,
                isChecked = true
            )
        ),
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_CONTACT,
        nameRes = R.string.contact_label,
        descriptionRes = R.string.contact_descr,
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_RATE_ON_PLAYSTORE,
        nameRes = R.string.rate_label,
        descriptionRes = R.string.rate_descr,
    ),
    SettingsItem(
        type = SettingsItemType.SETTING_APP_VERSION,
        nameRes = R.string.app_version_label,
        descriptionRes = R.string.app_version_desc,
    )
)
