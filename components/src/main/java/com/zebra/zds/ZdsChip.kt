package com.zebra.zds

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.google.android.material.chip.Chip

class ZdsChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyleAttr) {

    enum class Style {
        SELECTABLE, FILTER
    }

    var style: Style = Style.SELECTABLE
        set(value) {
            field = value
            applyStyle(field)
        }

    init {
        attrs?.let {
            val a: TypedArray = context.obtainStyledAttributes(it, R.styleable.ZdsChip)

            style = when (a.getInt(R.styleable.ZdsChip_chip_style, 0)) {
                1 -> Style.FILTER
                else -> Style.SELECTABLE
            }

            a.recycle()
        }

        applyStyle(style)
    }

    private fun applyStyle(style: Style) {
        isCheckable = true
        isCheckedIconVisible = style == Style.FILTER
    }
}
