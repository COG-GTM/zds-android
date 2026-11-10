package com.zebra.zds

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.widget.ImageViewCompat
import com.google.android.material.color.MaterialColors

class ZdsSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.searchViewStyle
) : SearchView(context, attrs, defStyleAttr) {

    enum class Style {
        ROUND, SHARP
    }

    var style: Style = Style.ROUND
        set(value) {
            field = value
            applyStyle()
        }

    private val searchText: EditText = findViewById(androidx.appcompat.R.id.search_src_text)
    private val closeButton: ImageView = findViewById(androidx.appcompat.R.id.search_close_btn)
    private val searchIcon: ImageView = findViewById(androidx.appcompat.R.id.search_mag_icon)
    private val searchButton: ImageView = findViewById(androidx.appcompat.R.id.search_button)

    private val backgroundDrawable = GradientDrawable()
    private val strokeWidth = resources.getDimensionPixelSize(R.dimen.search_view_stroke_width)
    private var queryTextFocusChangeListener: OnFocusChangeListener? = null

    init {
        // Inner containers can inherit an opaque android:background from a parent theme overlay
        // (e.g. Zds.Toolbar), which would hide the outline drawn on this view.
        listOf(
            androidx.appcompat.R.id.search_bar,
            androidx.appcompat.R.id.search_edit_frame,
            androidx.appcompat.R.id.search_plate,
            androidx.appcompat.R.id.submit_area
        ).forEach { findViewById<View>(it).background = null }

        backgroundDrawable.setColor(
            MaterialColors.getColor(
                this,
                com.google.android.material.R.attr.colorSurface,
                ContextCompat.getColor(context, R.color.background)
            )
        )
        background = backgroundDrawable

        applyColors()

        super.setOnQueryTextFocusChangeListener { view, hasFocus ->
            updateBackground(hasFocus)
            queryTextFocusChangeListener?.onFocusChange(view, hasFocus)
        }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = updateCloseButton()
        })

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZdsSearchView, defStyleAttr, 0)
        style = Style.values()[typedArray.getInt(R.styleable.ZdsSearchView_zdsSearchStyle, Style.ROUND.ordinal)]
        typedArray.recycle()

        updateBackground(searchText.hasFocus())
        updateCloseButton()
    }

    override fun setOnQueryTextFocusChangeListener(listener: OnFocusChangeListener?) {
        queryTextFocusChangeListener = listener
    }

    override fun setIconified(iconify: Boolean) {
        super.setIconified(iconify)
        updateCloseButton()
    }

    override fun onActionViewExpanded() {
        super.onActionViewExpanded()
        updateCloseButton()
    }

    private fun applyStyle() {
        val isSharp = style == Style.SHARP
        backgroundDrawable.cornerRadius =
            if (isSharp) 0f else resources.getDimension(R.dimen.search_view_corner_radius)

        val searchIconRes = if (isSharp) R.drawable.ic_search_sharp else R.drawable.ic_search_round
        searchIcon.setImageResource(searchIconRes)
        searchButton.setImageResource(searchIconRes)
        closeButton.setImageResource(if (isSharp) R.drawable.ic_cancel_sharp else R.drawable.ic_cancel_round)
    }

    private fun applyColors() {
        val iconTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.zebra_grey_subtle_icon))
        ImageViewCompat.setImageTintList(searchIcon, iconTint)
        ImageViewCompat.setImageTintList(searchButton, iconTint)
        ImageViewCompat.setImageTintList(closeButton, iconTint)

        val accent = ContextCompat.getColor(context, R.color.zebra_blue_enabled)
        searchText.setTextColor(ContextCompat.getColor(context, R.color.textPrimaryVariant))
        searchText.setHintTextColor(ContextCompat.getColor(context, R.color.zebra_grey_subtle_icon))
        searchText.highlightColor = ColorUtils.setAlphaComponent(accent, HIGHLIGHT_ALPHA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchText.textCursorDrawable = GradientDrawable().apply {
                setColor(accent)
                setSize(strokeWidth, searchText.lineHeight)
            }
        }
    }

    private fun updateBackground(focused: Boolean) {
        val strokeColor = if (focused) R.color.zebra_blue_enabled else R.color.zebra_grey_subtle_stroke
        backgroundDrawable.setStroke(strokeWidth, ContextCompat.getColor(context, strokeColor))
    }

    private fun updateCloseButton() {
        closeButton.visibility = if (searchText.text.isNullOrEmpty()) GONE else VISIBLE
    }

    private companion object {
        const val HIGHLIGHT_ALPHA = 0x66
    }
}
