package com.lesson3.exercise1.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.lesson3.exercise1.R
import com.lesson3.exercise1.presentation.ui.sign_in.SignInFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        supportFragmentManager.commit {
            add<SignInFragment>(R.id.container)
        }

    }

    private fun fitContentViewToInsets() {
        WindowCompat.setDecorFitsSystemWindows(window,false)
    }
}