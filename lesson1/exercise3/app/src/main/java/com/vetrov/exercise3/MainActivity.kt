package com.vetrov.exercise3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var userInput: TextView
    private lateinit var userInputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.toHelloActivityButton)
        userInput = findViewById(R.id.userNameInput)
        userInputLayout = findViewById(R.id.userNameInputLayout)

        button.setOnClickListener {
            if(validateUserName()) {
                val intent = Intent(this, HelloActivity::class.java)
                intent.putExtra(HelloActivity.USER_NAME, userInput.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun validateUserName(): Boolean {
        val userName = userInput.text.toString()
        if(userName.trim().isEmpty()){
            userInputLayout.error = "Field should`t be empty"
            userInput.requestFocus()
            return false
        }
        else if(userName.contains("[0-9!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~]".toRegex())){
            userInputLayout.error = "Field should`t contain digits and invalid symbols"
            userInput.requestFocus()
            return false
        }
        else {
            userInputLayout.isErrorEnabled = false
        }
        return true
    }
}