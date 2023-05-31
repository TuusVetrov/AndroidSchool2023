package com.example.hxh_project.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hxh_project.R
import kotlin.properties.Delegates

class Counter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): LinearLayout(context, attrs, defStyleAttr) {
    var maxQuantity: Int by Delegates.observable(MIN_COUNTER_VALUE) { _, _, newValue ->
        if (newValue < MIN_COUNTER_VALUE) {
            maxQuantity = MIN_COUNTER_VALUE
        } else {
            updateButtonState()
        }
    }

   private var counter: Int by Delegates.observable(MIN_COUNTER_VALUE) { _, _, newValue ->
       onCountChangedListener?.invoke(newValue)
    }

    fun getCounterValue() = counter

    fun setCounterValue(value: Int) {
        counter = if (value < MIN_COUNTER_VALUE)  MIN_COUNTER_VALUE else value
        tvCount.text = value.toString()
    }

    private var onCountChangedListener: ((count: Int) -> Unit)? = null

    fun setOnCountChangedListener(listener: (count: Int) -> Unit) {
        onCountChangedListener = listener
    }

    private val minusBtn: Button
    private val tvCount: TextView
    private val plusBtn: Button

    init {
        val root =  LayoutInflater.from(context).inflate(
                    R.layout.view_count,
                    this,
                    true)

        minusBtn = root.findViewById(R.id.btnMinus)
        tvCount = root.findViewById(R.id.tvCount)
        plusBtn = root.findViewById(R.id.btnPlus)

        tvCount.text = counter.toString()

        updateButtonState()

        setListeners()
    }

    private fun setListeners() {
        minusBtn.setOnClickListener {
            counter--
            updateButtonState()
            tvCount.text = counter.toString()
        }

        plusBtn.setOnClickListener {
            counter++
            updateButtonState()
            tvCount.text = counter.toString()
        }
    }

    private fun updateButtonState() {
        minusBtn.isEnabled = counter > MIN_COUNTER_VALUE
        plusBtn.isEnabled = counter < maxQuantity
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)

        if (childCount > MAX_CHILD_COUNT) {
            throw IllegalStateException("Counter can't host child")
        }
    }

    companion object {
        private const val MIN_COUNTER_VALUE = 1
        private const val MAX_CHILD_COUNT = 1
    }
}