package com.example.hxh_project.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.hxh_project.R
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        supportFragmentManager.commit {
            add<CatalogFragment>(R.id.main_activity_container)
        }
    }

    private fun fitContentViewToInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}