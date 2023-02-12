package com.lesson2.exercise3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener

class NumberFragment : Fragment() {
    private lateinit var tvNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNumber = view.findViewById(R.id.tvNumber)

        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getInt(BUNDLE_KEY).toString()

            tvNumber.text = result
        }
    }

    companion object {
        const val REQUEST_KEY = "numberRequestKey"
        const val BUNDLE_KEY = "bundleKey"

        fun newInstance() = NumberFragment()
    }
}