package com.lesson2.exercise4

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LifeCycleFragment : Fragment() {
    private lateinit var tvFragmentLifeCycle: TextView
    private var _buffer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _buffer = "onCreate\n"
        Log.d("LifeCycle", "LifeCycleFragment: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _buffer += "onCreateView\n"
        Log.d("LifeCycle", "LifeCycleFragment: onCreateView")
        return inflater.inflate(R.layout.fragment_life_cicle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvFragmentLifeCycle = view.findViewById(R.id.tvFragmentLifeCycle)

        tvFragmentLifeCycle.movementMethod = ScrollingMovementMethod()

        tvFragmentLifeCycle.append(_buffer ?: "")
        _buffer = null

        tvFragmentLifeCycle.append("onViewCreated\n")

        Log.d("LifeCycle", "LifeCycleFragment: onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tvFragmentLifeCycle.append("onViewStateRestored\n")
        Log.d("LifeCycle", "LifeCycleFragment: onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        tvFragmentLifeCycle.append("onStart\n")
        Log.d("LifeCycle", "LifeCycleFragment: onStart")
    }

    override fun onResume() {
        super.onResume()
        tvFragmentLifeCycle.append("onResume\n")
        Log.d("LifeCycle", "LifeCycleFragment: onResume")
    }

    override fun onPause() {
        super.onPause()
        tvFragmentLifeCycle.append("onPause\n")
        Log.d("LifeCycle", "LifeCycleFragment: onPause")
    }

    override fun onStop() {
        super.onStop()
        tvFragmentLifeCycle.append("onStop\n")
        Log.d("LifeCycle", "LifeCycleFragment: onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tvFragmentLifeCycle.append("onSaveInstanceState\n")
        Log.d("LifeCycle", "LifeCycleFragment: onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvFragmentLifeCycle.append("onDestroyView\n")
        Log.d("LifeCycle", "LifeCycleFragment: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        tvFragmentLifeCycle.append("onDestroy\n")
        Log.d("LifeCycle", "LifeCycleFragment: onDestroy")
    }

    companion object {
        fun newInstance() = LifeCycleFragment()
    }
}