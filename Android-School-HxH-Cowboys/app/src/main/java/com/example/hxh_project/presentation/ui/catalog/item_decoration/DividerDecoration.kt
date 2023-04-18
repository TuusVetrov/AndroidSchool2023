package com.example.hxh_project.presentation.ui.catalog.item_decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.R

class DividerDecoration(
    private val context: Context,
) : RecyclerView.ItemDecoration() {

    private val paintDivider = Paint().apply {
        color = ContextCompat.getColor(context, R.color.seashell)
    }

    private var dividerHeight = context.resources.getDimension(R.dimen.divider_height)
    private var marginHorizontal = context.resources.getDimension(R.dimen.divider_horizontal_margin)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val lastChildIndex = parent.adapter?.itemCount?.minus(1) ?: -1

        for (i in 1 until childCount) {
            if (i == lastChildIndex) {
                continue
            }

            val child = parent.getChildAt(i)
            val dividerTop = child.bottom.toFloat()
            val dividerBottom = dividerTop + dividerHeight
            val dividerLeft = child.left + marginHorizontal
            val dividerRight = child.right - marginHorizontal
            c.drawRect(
                dividerLeft,
                dividerTop,
                dividerRight,
                dividerBottom,
                paintDivider
            )
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position < (parent.adapter?.itemCount?.minus(1) ?: 0)) {
            outRect.bottom = dividerHeight.toInt()
        }
    }
}