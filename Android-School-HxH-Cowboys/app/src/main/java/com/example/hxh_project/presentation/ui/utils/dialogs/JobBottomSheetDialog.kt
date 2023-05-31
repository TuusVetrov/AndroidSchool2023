package com.example.hxh_project.presentation.ui.utils.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import com.example.hxh_project.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class JobBottomSheetDialog(
    listener: SettingsBottomSheetDialogListener,
): BottomSheetDialogFragment() {
    private var bottomSheetListener: SettingsBottomSheetDialogListener?= null

    init {
        bottomSheetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet = findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                ) as FrameLayout
                addListenerToButtons(bottomSheet)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.job_bottom_sheet, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            bottomSheetListener = context as SettingsBottomSheetDialogListener?
        } catch (_: ClassCastException) { }
    }

    private fun addListenerToButtons(bottomSheet: FrameLayout) {
        val rootView = bottomSheet.getChildAt(0) as ConstraintLayout
        val view = rootView.getChildAt(0) as LinearLayout

        for (index in 0 until view.size) {
            val button = view.getChildAt(index) as Button
            button.setOnClickListener {
                bottomSheetListener?.onJobClick(button.text.toString())
            }
        }
    }

    interface SettingsBottomSheetDialogListener {
        fun onJobClick(jobTitle: String)
    }

    companion object {
        const val TAG = "JobBottomSheet"
    }
}