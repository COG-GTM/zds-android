package com.zebra.zds

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Horizontal step indicator for multi-step flows (checkout, onboarding, wizards).
 *
 * Each step is drawn as a node connected by a track. Nodes before [currentStep] are
 * rendered as completed (filled with a check mark), the current node is highlighted,
 * and later nodes are rendered as upcoming. Changing [currentStep] animates the track
 * between the two nodes.
 */
class ZdsStepper : View {

    enum class StepState { COMPLETED, ACTIVE, UPCOMING }

    fun interface OnStepClickListener {
        fun onStepClick(stepper: ZdsStepper, step: Int)
    }

    fun interface OnStepChangeListener {
        fun onStepChanged(stepper: ZdsStepper, step: Int)
    }

    var steps: List<String> = emptyList()
        set(value) {
            field = value
            if (currentStep > value.size - 1) {
                currentStep = max(0, value.size - 1)
            }
            animatedProgress = currentStep.toFloat()
            updateContentDescription()
            requestLayout()
            invalidate()
        }

    val stepCount: Int
        get() = steps.size

    private var currentStepValue = 0

    /** Zero-based index of the active step. Assigning moves without animation. */
    var currentStep: Int
        get() = currentStepValue
        set(value) = setCurrentStep(value, false)

    var isSharp: Boolean = false
        set(value) {
            field = value
            checkDrawable = loadCheckDrawable()
            invalidate()
        }

    var showLabels: Boolean = true
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /**
     * When true, tapping a completed step (or the active one) invokes [onStepClickListener].
     * Upcoming steps are never clickable, so a user cannot skip ahead.
     */
    var stepsClickable: Boolean = false

    var onStepClickListener: OnStepClickListener? = null
    var onStepChangeListener: OnStepChangeListener? = null

    private var animatedProgress: Float = 0f
    private var animator: ValueAnimator? = null

    private val nodeSize = dp(32f)
    private val nodeStroke = dp(2f)
    private val trackThickness = dp(2f)
    private val trackGap = dp(4f)
    private val labelGap = dp(8f)
    private val checkSize = dp(16f)

    private val colorActive = ContextCompat.getColor(context, R.color.zebra_blue_enabled)
    private val colorUpcoming = ContextCompat.getColor(context, R.color.zebra_grey_disabled)
    private val colorTrack = ContextCompat.getColor(context, R.color.zebra_grey_subtle_stroke)
    private val colorSurface = ContextCompat.getColor(context, R.color.zebra_control_inverse)
    private val colorOnActive = ContextCompat.getColor(context, R.color.textInverse)
    private val colorLabel = ContextCompat.getColor(context, R.color.textPrimaryVariant)
    private val colorLabelMuted = ContextCompat.getColor(context, R.color.zebra_hint)

    private val fontRegular: Typeface? = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_regular)
    private val fontMedium: Typeface? = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_medium)

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = nodeStroke
    }
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = trackThickness
    }
    private val numberPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = sp(14f)
        typeface = fontMedium
    }
    private val labelPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = sp(12f)
        typeface = fontRegular
    }

    private var checkDrawable = loadCheckDrawable()
    private val nodeRect = RectF()
    private var downStep = -1

    constructor(context: Context) : super(context) {
        initialize(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.ZdsStepper)

            val stepsResId = a.getResourceId(R.styleable.ZdsStepper_steps, 0)
            val stepCountAttr = a.getInt(R.styleable.ZdsStepper_stepCount, 0)
            steps = when {
                stepsResId != 0 -> resources.getStringArray(stepsResId).toList()
                stepCountAttr > 0 -> List(stepCountAttr) { "" }
                else -> emptyList()
            }

            isSharp = a.getBoolean(R.styleable.ZdsStepper_isSharp, false)
            showLabels = a.getBoolean(R.styleable.ZdsStepper_showLabels, true)
            stepsClickable = a.getBoolean(R.styleable.ZdsStepper_stepsClickable, false)
            currentStep = a.getInt(R.styleable.ZdsStepper_currentStep, 0)

            a.recycle()
        }
        updateContentDescription()
    }

    /**
     * Moves the indicator to [step] (0-based). Values outside the step range are clamped.
     * When [animate] is true the track fills or drains between the old and new position.
     */
    fun setCurrentStep(step: Int, animate: Boolean) {
        val clamped = clampStep(step)
        val changed = clamped != currentStepValue
        currentStepValue = clamped

        animator?.cancel()
        if (animate && isAttachedToWindow && animatedProgress != clamped.toFloat()) {
            animator = ValueAnimator.ofFloat(animatedProgress, clamped.toFloat()).apply {
                duration = (ANIMATION_DURATION_PER_STEP * abs(clamped - animatedProgress))
                    .roundToInt().coerceAtLeast(ANIMATION_DURATION_MIN).toLong()
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    animatedProgress = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            animatedProgress = clamped.toFloat()
            invalidate()
        }

        updateContentDescription()
        if (changed) {
            onStepChangeListener?.onStepChanged(this, clamped)
        }
    }

    fun next(): Boolean {
        if (currentStep >= stepCount - 1) return false
        setCurrentStep(currentStep + 1, true)
        return true
    }

    fun previous(): Boolean {
        if (currentStep <= 0) return false
        setCurrentStep(currentStep - 1, true)
        return true
    }

    val isLastStep: Boolean
        get() = stepCount > 0 && currentStep == stepCount - 1

    fun getStepState(step: Int): StepState = when {
        step < currentStep -> StepState.COMPLETED
        step == currentStep -> StepState.ACTIVE
        else -> StepState.UPCOMING
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = paddingLeft + paddingRight + (stepCount * dp(72f)).roundToInt()
        var desiredHeight = paddingTop + paddingBottom + nodeSize
        if (showLabels && steps.any { it.isNotEmpty() }) {
            desiredHeight += labelGap + labelPaint.fontMetrics.let { it.descent - it.ascent }
        }
        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight.roundToInt(), heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = stepCount
        if (count == 0) return

        val slotWidth = (width - paddingLeft - paddingRight) / count.toFloat()
        val radius = nodeSize / 2f
        val centerY = paddingTop + radius

        trackPaint.strokeCap = if (isSharp) Paint.Cap.BUTT else Paint.Cap.ROUND

        for (i in 0 until count - 1) {
            val startX = centerX(i, slotWidth) + radius + trackGap
            val endX = centerX(i + 1, slotWidth) - radius - trackGap
            if (endX <= startX) continue

            trackPaint.color = colorTrack
            canvas.drawLine(startX, centerY, endX, centerY, trackPaint)

            val fill = (animatedProgress - i).coerceIn(0f, 1f)
            if (fill > 0f) {
                trackPaint.color = colorActive
                canvas.drawLine(startX, centerY, startX + (endX - startX) * fill, centerY, trackPaint)
            }
        }

        for (i in 0 until count) {
            val cx = centerX(i, slotWidth)
            drawNode(canvas, i, cx, centerY, radius)
            if (showLabels && steps[i].isNotEmpty()) {
                drawLabel(canvas, i, cx, centerY + radius + labelGap, slotWidth)
            }
        }
    }

    private fun drawNode(canvas: Canvas, index: Int, cx: Float, cy: Float, radius: Float) {
        val state = visualState(index)
        val inset = nodeStroke / 2f
        nodeRect.set(cx - radius + inset, cy - radius + inset, cx + radius - inset, cy + radius - inset)

        when (state) {
            StepState.COMPLETED -> {
                fillPaint.color = colorActive
                drawShape(canvas, fillPaint)
                checkDrawable?.let {
                    val half = (checkSize / 2f).roundToInt()
                    it.setBounds(
                        (cx - half).roundToInt(), (cy - half).roundToInt(),
                        (cx + half).roundToInt(), (cy + half).roundToInt()
                    )
                    it.draw(canvas)
                }
            }

            StepState.ACTIVE -> {
                fillPaint.color = colorSurface
                drawShape(canvas, fillPaint)
                strokePaint.color = colorActive
                drawShape(canvas, strokePaint)
                numberPaint.color = colorActive
                drawNumber(canvas, index, cx, cy)
            }

            StepState.UPCOMING -> {
                fillPaint.color = colorSurface
                drawShape(canvas, fillPaint)
                strokePaint.color = colorUpcoming
                drawShape(canvas, strokePaint)
                numberPaint.color = colorUpcoming
                drawNumber(canvas, index, cx, cy)
            }
        }
    }

    private fun drawShape(canvas: Canvas, paint: Paint) {
        if (isSharp) {
            canvas.drawRect(nodeRect, paint)
        } else {
            canvas.drawOval(nodeRect, paint)
        }
    }

    private fun drawNumber(canvas: Canvas, index: Int, cx: Float, cy: Float) {
        val baseline = cy - (numberPaint.ascent() + numberPaint.descent()) / 2f
        canvas.drawText((index + 1).toString(), cx, baseline, numberPaint)
    }

    private fun drawLabel(canvas: Canvas, index: Int, cx: Float, top: Float, slotWidth: Float) {
        val state = visualState(index)
        labelPaint.typeface = if (state == StepState.ACTIVE) fontMedium else fontRegular
        labelPaint.color = if (state == StepState.UPCOMING) colorLabelMuted else colorLabel

        val maxWidth = slotWidth - dp(4f)
        val text = TextUtils.ellipsize(steps[index], labelPaint, maxWidth, TextUtils.TruncateAt.END)
        canvas.drawText(text, 0, text.length, cx, top - labelPaint.ascent(), labelPaint)
    }

    /** Node appearance follows the animated track so nodes flip only once the track reaches them. */
    private fun visualState(index: Int): StepState = when {
        animatedProgress >= index + 1f - PROGRESS_EPSILON -> StepState.COMPLETED
        animatedProgress >= index - PROGRESS_EPSILON -> StepState.ACTIVE
        else -> StepState.UPCOMING
    }

    private fun centerX(index: Int, slotWidth: Float): Float =
        paddingLeft + slotWidth * index + slotWidth / 2f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!stepsClickable || !isEnabled || stepCount == 0) {
            return super.onTouchEvent(event)
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downStep = stepAt(event.x)
                return downStep >= 0
            }

            MotionEvent.ACTION_UP -> {
                val upStep = stepAt(event.x)
                if (upStep >= 0 && upStep == downStep) {
                    performClick()
                    onStepClickListener?.onStepClick(this, upStep)
                }
                downStep = -1
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                downStep = -1
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun stepAt(x: Float): Int {
        val slotWidth = (width - paddingLeft - paddingRight) / stepCount.toFloat()
        val index = ((x - paddingLeft) / slotWidth).toInt()
        if (index < 0 || index >= stepCount) return -1
        return if (index <= currentStep) index else -1
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        bundle.putInt(STATE_CURRENT_STEP, currentStep)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))
            setCurrentStep(state.getInt(STATE_CURRENT_STEP, 0), false)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    private fun clampStep(step: Int): Int = when {
        stepCount == 0 -> 0
        else -> step.coerceIn(0, stepCount - 1)
    }

    private fun updateContentDescription() {
        if (stepCount == 0) {
            contentDescription = null
            return
        }
        val label = steps[currentStep]
        contentDescription = if (label.isEmpty()) {
            resources.getString(R.string.stepper_description, currentStep + 1, stepCount)
        } else {
            resources.getString(R.string.stepper_description_labelled, currentStep + 1, stepCount, label)
        }
    }

    private fun loadCheckDrawable() = ContextCompat.getDrawable(
        context,
        if (isSharp) R.drawable.ic_check_mark_sharp else R.drawable.ic_check_mark_round
    )?.let {
        val wrapped = DrawableCompat.wrap(it).mutate()
        DrawableCompat.setTint(wrapped, colorOnActive)
        wrapped
    }

    private fun dp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

    private fun sp(value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)

    companion object {
        private const val ANIMATION_DURATION_PER_STEP = 300f
        private const val ANIMATION_DURATION_MIN = 150
        private const val PROGRESS_EPSILON = 0.001f
        private const val STATE_SUPER = "zds_stepper_super"
        private const val STATE_CURRENT_STEP = "zds_stepper_current_step"
    }
}
