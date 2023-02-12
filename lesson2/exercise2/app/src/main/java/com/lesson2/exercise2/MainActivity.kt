package com.lesson2.exercise2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    private lateinit var btnFirst: Button
    private lateinit var btnSecond: Button
    private lateinit var btnThird: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFirst = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        btnThird = findViewById(R.id.btnThird)

        supportFragmentManager.commit {
            replace(R.id.activityContainer, TextFragment.newInstance())
        }
        btnFirst.setOnClickListener {
            sendData(1)
        }

        btnSecond.setOnClickListener {
            sendData(2)
        }

        btnThird.setOnClickListener {
            sendData(3)
        }
    }

    private fun sendData(number: Int) {
        supportFragmentManager.setFragmentResult(
            TextFragment.REQUEST_KEY,
            bundleOf(TextFragment.BUNDLE_KEY to number))
    }
}