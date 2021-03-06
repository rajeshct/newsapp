package com.startup.news.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.startup.news.application.MainActivity
import com.startup.news.application.R
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.prefrences.SharedPrefrenceData
import com.startup.news.fragments.SelectCategory
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreen : AppCompatActivity() {

    lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        hTextView.animateText(getString(R.string.animationText))
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (SharedPrefrenceData().isCategorySelected()) {
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                } else {
                    val intent = Intent(this@SplashScreen, SelectCategory::class.java)
                    intent.putExtra(AppConstants.EXTRA_OPEN_SCREEN, AppConstants.OPEN_CATEGORY)
                    startActivity(intent)
                }
                finish()
            }
        }, 1000)
    }

    override fun onStop() {
        timer.cancel()
        timer.purge()
        super.onStop()
    }

}
