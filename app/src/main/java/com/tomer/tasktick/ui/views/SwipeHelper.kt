package com.tomer.tasktick.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tomer.tasktick.R
import kotlin.math.abs


class SwipeHelper(
    private val context: Context,
    private val swipeCA: (pos: Int) -> Unit
) : ItemTouchHelper.Callback() {

    private val delBottom: Drawable = ContextCompat.getDrawable(context, R.drawable.del_bottom)!!
    private val delUpper: Drawable = ContextCompat.getDrawable(context, R.drawable.del_top)!!
    private var currentItem: RecyclerView.ViewHolder? = null
    private var mView: View? = null
    private val p = Paint().apply {
        color = Color.RED
    }
    private val rect = Rect(0, 0, 0, 0)

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        mView = viewHolder.itemView
        return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        currentItem = viewHolder


        val f24 = 24f.toPX()
        val y = (((mView!!.height - (f24)) / 2) + currentItem!!.itemView.y).toInt()
        delBottom.setBounds((mView!!.width - (2 * f24)).toInt(), y, (mView!!.width - f24).toInt(), (y + (f24)).toInt())
        delUpper.bounds = delBottom.copyBounds()
        val dif = ((abs(dX)/mView!!.width) * f24).toInt()
        delUpper.bounds.set(delUpper.bounds.left,delUpper.bounds.top-dif,delUpper.bounds.right,delUpper.bounds.bottom-dif)


        if (abs(dX).toInt() == mView!!.width) {
            viewHolder.itemView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            swipeCA(viewHolder.adapterPosition)
        }
        rect.set(0, currentItem!!.itemView.y.toInt(), mView!!.width, currentItem!!.itemView.y.toInt() + mView!!.height)
        drawRepButton(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    private fun drawRepButton(c: Canvas) {
        c.drawRect(rect, p)
        delBottom.draw(c)
        delUpper.draw(c)
    }

    private fun Float.toPX(): Float = context.resources.displayMetrics.density * this

}