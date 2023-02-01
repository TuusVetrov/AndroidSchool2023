package com.vetrov.exercise3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HelloActivity : AppCompatActivity() {
    private lateinit var greetingsUser: TextView

    companion object {
        const val USER_NAME = "user_name"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        greetingsUser = findViewById(R.id.greetingsUserText)

        val name = intent.getStringExtra(USER_NAME)

        greetingsUser.text = "Hello, $name"

    }
}