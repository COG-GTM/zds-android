package com.zebra.zds

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

internal class BannerDismissDelegate(private val banner: View) {

    var onDismissListener: OnBannerDismissListener? = null

    private var animator: ValueAnimator? = null
    private var heightBeforeAnimation: Int? = null

    fun dismiss() {
        if (banner.visibility != View.VISIBLE || animator != null) return

        val height = banner.height
        if (height == 0 || animatorDurationScale() == 0f) {
            hide()
            return
        }

        animateHeight(height, 0) { hide() }
    }

    fun show() {
        cancel()
        restoreHeight()
        banner.alpha = 1f

        if (banner.visibility == View.VISIBLE) return
        banner.visibility = View.VISIBLE

        val height = measureBannerHeight()
        if (height == 0 || animatorDurationScale() == 0f) return

        animateHeight(0, height) { restoreHeight() }
    }

    private fun hide() {
        restoreHeight()
        banner.alpha = 1f
        banner.visibility = View.GONE
        onDismissListener?.onDismiss(banner)
    }

    private fun cancel() {
        animator?.cancel()
        animator = null
    }

    private fun animateHeight(from: Int, to: Int, onEnd: () -> Unit) {
        heightBeforeAnimation = banner.layoutParams?.height
        animator = ValueAnimator.ofInt(from, to).apply {
            duration = DISMISS_DURATION_MS
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                setLayoutHeight(animation.animatedValue as Int)
                banner.alpha =
                    if (to == 0) 1f - animation.animatedFraction else animation.animatedFraction
            }
            addListener(object : AnimatorListenerAdapter() {
                private var canceled = false

                override fun onAnimationCancel(animation: Animator) {
                    canceled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    animator = null
                    if (!canceled) onEnd()
                }
            })
            start()
        }
    }

    private fun restoreHeight() {
        heightBeforeAnimation?.let { setLayoutHeight(it) }
        heightBeforeAnimation = null
    }

    private fun setLayoutHeight(height: Int) {
        val params = banner.layoutParams ?: return
        params.height = height
        banner.layoutParams = params
    }

    private fun measureBannerHeight(): Int {
        val parentWidth = (banner.parent as? View)?.width ?: 0
        if (parentWidth == 0) return 0
        val margins = (banner.layoutParams as? ViewGroup.MarginLayoutParams)
        val width = parentWidth - (margins?.leftMargin ?: 0) - (margins?.rightMargin ?: 0)
        banner.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        return banner.measuredHeight
    }

    private fun animatorDurationScale(): Float = Settings.Global.getFloat(
        banner.context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )

    private companion object {
        const val DISMISS_DURATION_MS = 200L
    }
}
