package com.generator.wildfyrelite.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.generator.wildfyrelite.R
import com.generator.wildfyrelite.defaultSettings
import com.generator.wildfyrelite.model.DefaultSettings
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 2000)

    }
}