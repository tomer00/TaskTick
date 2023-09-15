package com.tomer.tasktick.ui.views

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.tomer.tasktick.R


class RvRowBG : View {
    //region CONSTRUCTOR

    constructor(context: Context) : super(context) {
        commonInit(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        commonInit(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        commonInit(attributeSet)
    }

    private fun commonInit(attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RvRowBG)

        paintBlur.apply {
            color = typedArray.getColor(R.styleable.RvRowBG_colorDots, ContextCompat.getColor(context, R.color.primary_dark))
            radius = typedArray.getDimension(R.styleable.RvRowBG_radius, 22f).toPX()
            maskFilter = BlurMaskFilter(radius/2, Blur.NORMAL)
        }
        posInt = typedArray.getInt(R.styleable.RvRowBG_position, 0)
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("TAG--", "onSizeChanged: $w  $h")
        val p12 = 12f.toPX()
        when (posInt) {
            1 -> {
                val x = (width - radius) / 2
                val y = (height - radius) / 2
                rectOval.set(x, y, x + radius, y + radius)
            } //center
            2 -> {
                val x = width - p12
                val y = height - p12
                rectOval.set(x - radius, y - radius, x, y)
            } //bottomRight
            else -> rectOval.set(p12, p12, p12 + radius, p12 + radius)
        }
    }

    //endregion CONSTRUCTOR

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var posInt = 0
    private var radius = 0f
    private val rectOval = RectF(12f.toPX(), 12f.toPX(), 36f.toPX(), 36f.toPX())

    private val paintBlur = Paint().apply {
        color = Color.BLUE
        maskFilter = BlurMaskFilter(22f.toPX(), Blur.NORMAL)
        isAntiAlias = true
    }


    //region :: DRAW-->>

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(rectOval, paintBlur)
    }

    //endregion :: DRAW-->>

    private fun Float.toPX(): Float = context.resources.displayMetrics.density * this

    fun setCol(col: Int) {
        paintBlur.color = col
    }
}