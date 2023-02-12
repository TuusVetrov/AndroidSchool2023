package com.lesson2.exercise3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private lateinit var btnFirst: Button
    private lateinit var btnSecond: Button
    private lateinit var btnThird: Button

    private lateinit var btnLetterA: Button
    private lateinit var btnLetterB: Button
    private lateinit var btnLetterC: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFirst = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        btnThird = findViewById(R.id.btnThird)

        btnLetterA = findViewById(R.id.btnLetterA)
        btnLetterB = findViewById(R.id.btnLetterB)
        btnLetterC = findViewById(R.id.btnLetterC)

        supportFragmentManager.commit {
            replace(R.id.flLetter, LetterFragment.newInstance())
        }

        supportFragmentManager.commit {
            replace(R.id.flNumber, NumberFragment.newInstance())
        }

        btnFirst.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                NumberFragment.REQUEST_KEY,
                bundleOf(NumberFragment.BUNDLE_KEY to 1))
        }

        btnSecond.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                NumberFragment.REQUEST_KEY,
                bundleOf(NumberFragment.BUNDLE_KEY to 2))
        }

        btnThird.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                NumberFragment.REQUEST_KEY,
                bundleOf(NumberFragment.BUNDLE_KEY to 3))
        }

        btnLetterA.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                LetterFragment.REQUEST_KEY,
                bundleOf(LetterFragment.BUNDLE_KEY to "A"))
        }

        btnLetterB.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                LetterFragment.REQUEST_KEY,
                bundleOf(LetterFragment.BUNDLE_KEY to "B"))
        }

        btnLetterC.setOnClickListener {
            supportFragmentManager.setFragmentResult(
                LetterFragment.REQUEST_KEY,
                bundleOf(LetterFragment.BUNDLE_KEY to "C"))
        }

    }

}