package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

open class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyleAttr) {

    override fun performCloseIconClick(): Boolean = isEnabled && super.performCloseIconClick()
}
