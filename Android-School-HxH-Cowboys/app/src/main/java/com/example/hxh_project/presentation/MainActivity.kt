package com.example.hxh_project.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.hxh_project.R
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SnackbarListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        supportFragmentManager.commit {
            add<SignInFragment>(R.id.main_activity_container)
        }
    }

    private fun fitContentViewToInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun showSuccess(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(getColor(R.color.secondary))
            animationMode = Snackbar.ANIMATION_MODE_FADE
            show()
        }
    }

    override fun showError(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(getColor(R.color.error))
            animationMode = Snackbar.ANIMATION_MODE_FADE
            show()
        }
    }
}