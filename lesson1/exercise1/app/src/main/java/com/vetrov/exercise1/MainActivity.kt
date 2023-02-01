package com.vetrov.exercise1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var redButton: Button
    private lateinit var greenButton: Button
    private lateinit var blueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redButton = findViewById(R.id.redButton)
        greenButton = findViewById(R.id.greenButton)
        blueButton = findViewById(R.id.blueButton)
        
        redButton.setOnClickListener {
            val intent = Intent(this, RedActivity::class.java)
            startActivity(intent)
        }

        greenButton.setOnClickListener {
            val intent = Intent(this, GreenActivity::class.java)
            startActivity(intent)
        }

        blueButton.setOnClickListener {
            val intent = Intent(this, BlueActivity::class.java)
            startActivity(intent)
        }
    }
}