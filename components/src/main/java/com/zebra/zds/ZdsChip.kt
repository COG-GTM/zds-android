package com.zebra.zds

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip

open class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyle) {

    init {
        isEnabled = isEnabled
    }

    override fun setEnabled(enabled: Boolean) {

        if (!enabled) {
            val colorStateList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textDisabled))
            setTextColor(colorStateList)
            chipIconTint = colorStateList
            closeIconTint = colorStateList
            chipStrokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.zebra_grey_subtle_stroke)
            )
            chipBackgroundColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.surfaceDisabled))
        }

        super.setEnabled(enabled)
    }
}
