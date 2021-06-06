package com.momlok.arkanoid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momlok.arkanoid.Helper.Companion.paddleColor
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        redRB.setOnClickListener { setColor("red") }
        blueRB.setOnClickListener { setColor("blue")}
        greenRB.setOnClickListener { setColor("green") }
        blackRB.setOnClickListener { setColor("black") }

        goMenuBT.setOnClickListener {
            goMenu()
        }
    }
    private fun setColor(color: String){
        paddleColor = color
    }
    private fun goMenu(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}