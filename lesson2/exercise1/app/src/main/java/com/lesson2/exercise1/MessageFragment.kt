package com.lesson2.exercise1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

class MessageFragment : Fragment() {

    private lateinit var btnNext: Button
    private lateinit var tvMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext = view.findViewById(R.id.btnNext)
        tvMessage = view.findViewById(R.id.tvMessage)

        setFragmentResultListener(EditTextFragment.REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(EditTextFragment.TEXT_KEY).orEmpty()
            tvMessage.text = result
        }

        btnNext.setOnClickListener {
            val result = tvMessage.text.toString()
            setFragmentResult(REQUEST_KEY, bundleOf(EditTextFragment.TEXT_KEY to result))

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, EditTextFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

    }

    companion object {
        const val REQUEST_KEY = "text"

        fun newInstance() = MessageFragment()
    }
}