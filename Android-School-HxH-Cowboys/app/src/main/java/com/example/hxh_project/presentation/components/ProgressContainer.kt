package com.example.hxh_project.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.hxh_project.R
import kotlin.properties.Delegates

private const val MAX_CHILD_COUNT = 3

class ProgressContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layoutLoading: ViewGroup
    private val layoutNotice: ViewGroup

    private val imageView: ImageView
    private val messageTitle: TextView
    private val messageDescription: TextView
    private val buttonUpdate: Button

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.view_progress_container, this, true)
        layoutLoading = root.findViewById(R.id.layoutLoading)
        layoutNotice = root.findViewById(R.id.layoutNotice)

        imageView = root.findViewById(R.id.imageViewNotice)
        messageTitle = root.findViewById(R.id.textViewNoticeMessage)
        messageDescription = root.findViewById(R.id.textViewNoticeDescription)
        buttonUpdate = root.findViewById(R.id.buttonNoticeUpdate)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)

        if (childCount > MAX_CHILD_COUNT) {
            throw IllegalStateException("ProgressContainer can host only one direct child")
        }
    }

    private fun findContentView(): View? =
        children.firstOrNull {
            it.id != R.id.layoutLoading && it.id != R.id.layoutNotice
        }

    var state: State by Delegates.observable(State.Loading) { _, _, state ->
        when (state) {
            is State.Loading -> {
                layoutLoading.isVisible = true
                layoutNotice.isVisible = false
                findContentView()?.isVisible = false
            }
            is State.Notice -> {
                layoutLoading.isVisible = false
                layoutNotice.isVisible = true
                findContentView()?.isVisible = false

                imageView.setImageResource(state.imageId)
                messageTitle.text = resources.getText(state.message)
                messageDescription.text = resources.getText(state.description)
                buttonUpdate.setOnClickListener(state.buttonClickListener)
            }
            is State.Success -> {
                layoutLoading.isVisible = false
                layoutNotice.isVisible = false
                findContentView()?.isVisible = true
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Notice(val imageId: Int,
                          val message: Int,
                          val description: Int,
                          val buttonClickListener: OnClickListener,
                          ): State()
        object Success: State()
    }
}