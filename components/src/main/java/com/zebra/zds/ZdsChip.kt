package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip

/**
 * Zebra Design System chip. Use with one of the `Zds.Chip` styles:
 * - `Zds.Chip.Assist` — action chip with an optional leading icon
 * - `Zds.Chip.Filter` — checkable chip that shows a check mark when selected
 * - `Zds.Chip.Input` — dismissible chip with a close icon
 */
class ZdsChip @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : Chip(context, attrs)
