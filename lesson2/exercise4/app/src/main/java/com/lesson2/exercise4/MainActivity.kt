package com.lesson2.exercise4

import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity() {

    private lateinit var tvLifeCycleActivity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLifeCycleActivity = findViewById(R.id.tvLifeCycleActivity)

        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.container, LifeCycleFragment.newInstance())
            }
        }
        tvLifeCycleActivity.movementMethod = ScrollingMovementMethod()
        tvLifeCycleActivity.append("onCreate\n")
        Log.d("LifeCycle", "MainActivity: onCreate")
    }

    override fun onStart() {
        super.onStart()
        tvLifeCycleActivity.append("onStart\n")
        Log.d("LifeCycle", "MainActivity: onStart")
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        tvLifeCycleActivity.append("onPostCreate\n")
        Log.d("LifeCycle", "MainActivity: onPostCreate")
    }

    override fun onResume() {
        super.onResume()
        tvLifeCycleActivity.append("onResume\n")
        Log.d("LifeCycle", "MainActivity: onResume")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        tvLifeCycleActivity.append("onResumeFragments\n")
        Log.d("LifeCycle", "MainActivity: onResumeFragments")
    }

    override fun onPostResume() {
        super.onPostResume()
        tvLifeCycleActivity.append("onPostResume\n")
        Log.d("LifeCycle", "MainActivity: onPostResume")
    }

    override fun onPause() {
        super.onPause()
        tvLifeCycleActivity.append("onPause\n")
        Log.d("LifeCycle", "MainActivity: onPause")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        tvLifeCycleActivity.append("onSaveInstanceState\n")
        Log.d("LifeCycle", "MainActivity: onSaveInstanceState")
    }

    override fun onStop() {
        super.onStop()
        tvLifeCycleActivity.append("onStop\n")
        Log.d("LifeCycle", "MainActivity: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        tvLifeCycleActivity.append("onRestart\n")
        Log.d("LifeCycle", "MainActivity: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        tvLifeCycleActivity.append("onDestroy\n")
        Log.d("LifeCycle", "MainActivity: onDestroy")
    }
}