package com.example.hxh_project.presentation.ui.catalog.item_decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.R

class MarginItemDecoration(
    private val context: Context,
) : RecyclerView.ItemDecoration() {

    private val paintMarginBody = Paint().apply {
        color = ContextCompat.getColor(context, R.color.white)
    }
    private var margin = context.resources.getDimension(R.dimen.normal_200)
    private var cornerRadius = context.resources.getDimension(R.dimen.normal_200)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position == 0) {
                c.drawRect(
                    child.left.toFloat(),
                    (child.top - margin),
                    child.right.toFloat(),
                    child.top.toFloat(),
                    paintMarginBody
                )
            }

            if (position == parent.adapter?.itemCount?.minus(1)) {
                val rectF = RectF(
                    child.left.toFloat(),
                    child.bottom.toFloat() - cornerRadius,
                    child.right.toFloat(),
                    (child.bottom + margin)
                )
                c.drawRoundRect(rectF, cornerRadius, cornerRadius, paintMarginBody)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.top = margin.toInt()
        }

        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = margin.toInt()
        }
    }
}