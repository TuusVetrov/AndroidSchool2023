package com.example.hxh_project.presentation.ui.catalog.item_decoration

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val context: Context,
) : RecyclerView.ItemDecoration() {

    private val paintMarginBody = Paint()
    private var margin = 0
    private var cornerRadius = 0f

    fun setColor(colorId: Int) {
        this.paintMarginBody.apply { color = ContextCompat.getColor(context, colorId) }
    }

    fun setMargin(dp: Int) {
        this.margin = dpToPx(dp)
    }

    fun setCornerRadius(dp: Int) {
        this.cornerRadius = dpToPx(dp).toFloat()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position == 0) {
                c.drawRect(
                    child.left.toFloat(),
                    (child.top - margin).toFloat(),
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
                    (child.bottom + margin).toFloat()
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
            outRect.top = margin
        }

        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = margin
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}