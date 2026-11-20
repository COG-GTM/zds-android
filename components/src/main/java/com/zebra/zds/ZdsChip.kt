package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

/**
 * Zebra Design System chip. Style it with one of the `Zds.Chip.*` styles:
 * [Assist][R.style.Zds_Chip_Assist], [Filter][R.style.Zds_Chip_Filter] or
 * [Input][R.style.Zds_Chip_Input] (each with a `.Sharp` variant).
 */
open class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.zdsChipStyle
) : Chip(context, attrs, defStyleAttr) {

    fun interface OnRemoveListener {
        fun onRemove(chip: ZdsChip)
    }

    /** Called when the close icon of an input chip is tapped. */
    fun setOnRemoveListener(listener: OnRemoveListener?) {
        setOnCloseIconClickListener(listener?.let { { _: android.view.View -> it.onRemove(this) } })
    }
}
