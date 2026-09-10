package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

open class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyle)
