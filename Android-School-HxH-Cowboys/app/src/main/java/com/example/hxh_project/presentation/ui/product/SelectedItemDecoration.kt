package com.example.hxh_project.presentation.ui.product

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.R

class SelectedItemDecoration(
    context: Context,
    var selectedPosition: Int,
): RecyclerView.ItemDecoration() {
    private val selectedBorderColor = ContextCompat.getColor(context, R.color.seashell)
    private val borderSize = context.resources.getDimensionPixelSize(R.dimen.preview_border_size)
    private val cornerRadius = context.resources.getDimension(R.dimen.preview_corner_radius)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        outRect.right = borderSize

        if (position == 0) {
            outRect.left = borderSize
        }

        outRect.top = borderSize
        outRect.bottom = borderSize
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val selectedView = parent.getChildAt(selectedPosition)
        selectedView?.let {
            val selectedRect = RectF(
                it.left.toFloat(),
                it.top.toFloat(),
                it.right.toFloat(),
                it.bottom.toFloat()
            )
            val paint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = borderSize.toFloat()
                color = selectedBorderColor
            }
            c.drawRoundRect(selectedRect, cornerRadius, cornerRadius, paint)
        }
    }
}