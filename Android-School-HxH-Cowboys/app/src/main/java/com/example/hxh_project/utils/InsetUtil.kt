package com.example.hxh_project.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

typealias OnSystemInsetsChangedListener = (statusBarSize: Int, navigationBarSize: Int) -> Unit

object InsetUtil {
    fun removeSystemInsets(view: View, listener: OnSystemInsetsChangedListener) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val systemInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val desiredBottomInset = calculateDesiredBottomInset(
                view,
                systemInsets.top,
                systemInsets.bottom,
                listener
            )

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = 0
                leftMargin = 0
                bottomMargin = desiredBottomInset
                rightMargin = 0
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun calculateDesiredBottomInset(
        view: View,
        topInset: Int,
        bottomInset: Int,
        listener: OnSystemInsetsChangedListener
    ): Int {
        val hasKeyboard = isKeyboardAppeared(view, bottomInset)
        val desiredBottomInset = if (hasKeyboard) bottomInset else 0
        listener(topInset, if (hasKeyboard) 0 else bottomInset)
        return desiredBottomInset
    }

    private fun isKeyboardAppeared(view: View, bottomInset: Int) =
        bottomInset / view.resources.displayMetrics.heightPixels.toDouble() > .25
}