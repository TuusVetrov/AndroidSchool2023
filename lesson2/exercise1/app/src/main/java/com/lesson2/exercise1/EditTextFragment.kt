package com.lesson2.exercise1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

class EditTextFragment: Fragment() {
    private lateinit var etMessage: EditText
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave = view.findViewById(R.id.btnSave)
        etMessage = view.findViewById(R.id.etMessage)

        setFragmentResultListener(MessageFragment.REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(TEXT_KEY)

            etMessage.setText(result.toString())
        }

        btnSave.setOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(TEXT_KEY to etMessage.text.toString()))
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, MessageFragment.newInstance())
                .addToBackStack(null)
                .commit()

        }
    }

    companion object {
        const val REQUEST_KEY = "changedText"
        const val TEXT_KEY = "textKey"

        fun newInstance() = EditTextFragment()
    }
}