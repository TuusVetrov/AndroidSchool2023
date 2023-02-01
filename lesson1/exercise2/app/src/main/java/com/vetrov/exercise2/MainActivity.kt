package com.vetrov.exercise2

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var browserButton: Button
    private lateinit var urlInput: TextView
    private lateinit var urlInputLayout: TextInputLayout

    private lateinit var callButton: Button
    private lateinit var telInput: TextView
    private lateinit var telInputLayout: TextInputLayout

    private lateinit var sendButton: Button
    private lateinit var emailInput: TextView
    private lateinit var emailInputLayout: TextInputLayout

    private lateinit var mapButton: Button
    private lateinit var latitudeInput: TextView
    private lateinit var latitudeInputLayout: TextInputLayout
    private lateinit var longitudeInput: TextView
    private lateinit var longitudeInputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browserButton = findViewById(R.id.urlButton)
        urlInput = findViewById(R.id.urlInput)
        urlInputLayout = findViewById(R.id.urlInputLayout)

        callButton = findViewById(R.id.telButton)
        telInput = findViewById(R.id.telInput)
        telInputLayout = findViewById(R.id.telInputLayout)

        sendButton = findViewById(R.id.emailButton)
        emailInput = findViewById(R.id.emailInput)
        emailInputLayout = findViewById(R.id.emailInputLayout)

        mapButton = findViewById(R.id.mapButton)
        latitudeInput = findViewById(R.id.latitudeInput)
        latitudeInputLayout = findViewById(R.id.latitudeInputLayout)
        longitudeInput = findViewById(R.id.longitudeInput)
        longitudeInputLayout = findViewById(R.id.longitudeInputLayout)

        browserButton.setOnClickListener {
            if(validateUrl()) {
                val intentUri = Uri.parse(urlInput.text.toString())
                val browserIntent = Intent(Intent.ACTION_VIEW, intentUri)
                startActivity(browserIntent)
            }
        }

        callButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            } else {
                if(validateTel()) {
                    val telIntentUri = Uri.parse("tel:${telInput.text}")
                    val telIntent = Intent(Intent.ACTION_CALL, telIntentUri)
                    startActivity(telIntent)
                }
            }
        }

        sendButton.setOnClickListener {
            if(validateEmail()) {
                val mailIntentUri = Uri.parse("mailto:${emailInput.text}")
                val emailIntent = Intent(Intent.ACTION_SENDTO, mailIntentUri)
                startActivity(emailIntent)
            }
        }

        mapButton.setOnClickListener {
            if(validateLatitude() && validateLongitude()) {
                val gmmIntentUri = Uri.parse("geo:${latitudeInput.text}, " +
                        "${longitudeInput.text}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailStr = emailInput.text.toString()
        if (emailStr.trim().isEmpty()) {
            emailInputLayout.error = "Field should`t be empty"
            emailInput.requestFocus()
            return false
        } else if (!FieldValidators.isValidEmail(emailStr)) {
            emailInputLayout.error = "Invalid Email!"
            emailInput.requestFocus()
            return false
        } else {
            emailInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateLatitude(): Boolean {
        val latitudeStr = latitudeInput.text.toString()
        if (latitudeStr.trim().isEmpty()) {
            latitudeInputLayout.error = "Field should`t be empty"
            latitudeInput.requestFocus()
            return false
        }  else if (!FieldValidators.isNumeric(latitudeStr) || latitudeStr.toDouble() > 90.0
            || latitudeStr.toDouble() < -90.0) {
            latitudeInputLayout.error = "The latitude of a point must be between -90 and 90"
            latitudeInput.requestFocus()
            return false
        }  else {
            latitudeInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateLongitude(): Boolean {
        val longitudeStr = longitudeInput.text.toString()
        if (longitudeStr.trim().isEmpty()) {
            longitudeInputLayout.error = "Field should`t be empty"
            longitudeInput.requestFocus()
            return false
        } else if(!FieldValidators.isNumeric(longitudeStr) || longitudeStr.toDouble() > 180.0
            || longitudeStr.toDouble() < -180.0) {
            longitudeInputLayout.error = "The longitude of a point must be between -180 and 180"
            longitudeInput.requestFocus()
            return false
        } else {
            longitudeInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateTel(): Boolean {
        val telStr = telInput.text.toString()
        if (telStr.trim().isEmpty()) {
            telInputLayout.error = "Field should`t be empty"
            telInput.requestFocus()
            return false
        } else if (!FieldValidators.isValidTel(telStr)) {
            telInputLayout.error = "Invalid telephone number!"
            telInput.requestFocus()
            return false
        } else {
            telInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateUrl(): Boolean {
        val urlStr = urlInput.text.toString()
        if(urlStr.trim().isEmpty()) {
            urlInputLayout.error = "Field should`t be empty"
            urlInput.requestFocus()
            return false
        }
        else if(!FieldValidators.isValidUrl(urlStr)) {
            urlInputLayout.error = "Url isn`t correct"
            urlInput.requestFocus()
            return false
        }
        else {
            urlInputLayout.isErrorEnabled = false
        }
        return true
    }
}