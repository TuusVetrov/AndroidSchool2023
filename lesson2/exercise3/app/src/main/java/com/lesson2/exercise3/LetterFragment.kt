package com.lesson2.exercise3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener

class LetterFragment : Fragment() {
    private lateinit var tvLetter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_letter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLetter = view.findViewById(R.id.tvLetter)

        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(BUNDLE_KEY)

            tvLetter.text = result ?: ""
        }
    }

    companion object {
        const val REQUEST_KEY = "letterRequestKey"
        const val BUNDLE_KEY = "bundleKey"

        fun newInstance() = LetterFragment()
    }
}