package com.zebra.zds

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec
import androidx.appcompat.widget.LinearLayoutCompat

/**
 * Root layout of [ZdsDialog]. Caps the dialog width on wide screens (tablet / landscape)
 * so it does not stretch edge to edge.
 */
internal class ZdsDialogLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val maxWidth = resources.getDimensionPixelSize(R.dimen.zds_dialog_max_width)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        if (MeasureSpec.getMode(widthSpec) != MeasureSpec.UNSPECIFIED &&
            MeasureSpec.getSize(widthSpec) > maxWidth
        ) {
            widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.getMode(widthSpec))
        }
        super.onMeasure(widthSpec, heightMeasureSpec)
    }
}
