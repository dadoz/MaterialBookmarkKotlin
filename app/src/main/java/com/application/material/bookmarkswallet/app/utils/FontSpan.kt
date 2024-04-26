package com.application.material.bookmarkswallet.app.utils

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

open class FontSpan(private val font: Typeface?) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) = updateTypeface(textPaint)

    override fun updateDrawState(textPaint: TextPaint) = updateTypeface(textPaint)

    private fun updateTypeface(textPaint: TextPaint) {
        textPaint.apply {
            typeface = Typeface.create(font, typeface?.style?: WRONG_TYPEFACE)
        }
    }

    companion object {
        const val WRONG_TYPEFACE = 0
    }
}