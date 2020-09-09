package com.rczubak.parkhereapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rczubak.parkhereapp.R
import com.rczubak.parkhereapp.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivityIntent = Intent(this, MainActivity::class.java)

        startActivity(mainActivityIntent)
        finish()
    }
}