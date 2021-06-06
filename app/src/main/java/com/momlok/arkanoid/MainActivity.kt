package com.momlok.arkanoid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goGameBT.setOnClickListener {
            goGame()
        }
        goSettingsBT.setOnClickListener {
            goSettings()
        }
    }

    private fun goSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun goGame(){
        val intent = Intent(applicationContext, GameActivity::class.java)
        startActivity(intent)
    }
}