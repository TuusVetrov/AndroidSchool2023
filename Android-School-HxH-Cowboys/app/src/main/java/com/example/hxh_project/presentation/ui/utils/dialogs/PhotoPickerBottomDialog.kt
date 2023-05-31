package com.example.hxh_project.presentation.ui.utils.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.hxh_project.R
import com.example.hxh_project.databinding.PhotoPickerBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PhotoPickerBottomDialog(
    listener: PhotoPickerBottomDialogListener,
): BottomSheetDialogFragment() {
    lateinit var binding: PhotoPickerBottomSheetBinding
    private var bottomSheetListener: PhotoPickerBottomDialogListener?= null

    init {
        bottomSheetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                addListenerToButtons()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PhotoPickerBottomSheetBinding.bind(
            inflater.inflate(R.layout.photo_picker_bottom_sheet, container)
        )
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            bottomSheetListener = context as PhotoPickerBottomDialogListener?
        } catch (_: ClassCastException) { }
    }

    private fun addListenerToButtons() {
        binding.btnTakePhoto.setOnClickListener {
            bottomSheetListener?.onTakePhoto()
        }

        binding.btnGetFromGallery.setOnClickListener {
            bottomSheetListener?.onGetFromGallery()
        }
    }

    interface PhotoPickerBottomDialogListener {
        fun onTakePhoto()
        fun onGetFromGallery()
    }

    companion object {
        const val TAG = "PhotoPickerBottomSheet"
    }
}