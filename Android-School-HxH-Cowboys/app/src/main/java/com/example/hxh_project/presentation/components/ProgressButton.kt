package com.example.hxh_project.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.hxh_project.R
import kotlin.properties.Delegates

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): FrameLayout(context, attrs, defStyleAttr) {
    private val button: Button
    private val progressBar: ProgressBar

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_progress_button, this, true)
        button = root.findViewById(R.id.button)
        progressBar = root.findViewById(R.id.progressBar)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProgressButton,
            0,0) .apply {
                try {
                    button.text = getString(R.styleable.ProgressButton_text)
                } finally {
                    recycle()
                }
        }
    }

    var isLoading: Boolean by Delegates.observable(false) { _, _, isLoading ->
        progressBar.isVisible = isLoading

        button.isClickable = !isLoading
        button.textScaleX = if (isLoading) 0f else 1f
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)

    }
}