package com.example.hxh_project.presentation.ui.catalog.item_decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(
    private val context: Context,
) : RecyclerView.ItemDecoration() {

    private val paintDivider = Paint()

    private var height = dpToPx(1)
    private var marginHorizontal = 0

    fun setColor(colorId: Int) {
        this.paintDivider.apply { color = ContextCompat.getColor(context, colorId) }
    }

    fun setHeight(dp: Int) {
        this.height = dpToPx(dp)
    }

    fun setMarginHorizontal(dp: Int) {
        this.marginHorizontal = dpToPx(dp)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val lastChildIndex = parent.adapter?.itemCount?.minus(1) ?: -1

        for (i in 0 until childCount) {
            if (i == lastChildIndex) {
                continue
            }

            val child = parent.getChildAt(i)
            val dividerTop = child.bottom.toFloat()
            val dividerBottom = dividerTop + height
            val dividerLeft = child.left + marginHorizontal
            val dividerRight = child.right - marginHorizontal
            c.drawRect(
                dividerLeft.toFloat(),
                dividerTop,
                dividerRight.toFloat(),
                dividerBottom,
                paintDivider
            )
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position < (parent.adapter?.itemCount?.minus(1) ?: 0)) {
            outRect.bottom = height
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}