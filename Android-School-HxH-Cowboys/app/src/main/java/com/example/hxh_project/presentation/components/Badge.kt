package com.example.hxh_project.presentation.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hxh_project.R

class Badge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): RelativeLayout(context, attrs, defStyleAttr) {
    private val tvBadge: TextView
    private val rootLayout: RelativeLayout
    private val cornerRadius: Float

    init {
        val root =  LayoutInflater.from(context).inflate(
            R.layout.bage_layout,
            this,
            true)

        tvBadge = root.findViewById(R.id.tvBadge)
        rootLayout = root.findViewById(R.id.badgeRoot)
        cornerRadius = resources.getDimension(R.dimen.normal_200)
    }

    fun setText(text: String) {
        tvBadge.text = text
    }

    fun setBackgroundColor(color: String) {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadius = cornerRadius
        gradientDrawable.setColor(Color.parseColor(color))

        rootLayout.background = gradientDrawable
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)

        if (childCount > MAX_CHILD_COUNT) {
            throw IllegalStateException("Counter can't host child")
        }
    }

    companion object {
        private const val MAX_CHILD_COUNT = 1
    }
}