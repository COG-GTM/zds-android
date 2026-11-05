package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyleAttr) {

    enum class Variant {
        SELECTABLE, FILTER
    }

    var variant: Variant = Variant.SELECTABLE
        set(value) {
            field = value
            applyVariant()
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZdsChip, defStyleAttr, 0)
        try {
            variant = Variant.values()[typedArray.getInt(R.styleable.ZdsChip_chip_variant, 0)]
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyVariant() {
        isCheckable = true
        isCheckedIconVisible = variant == Variant.FILTER
    }
}
