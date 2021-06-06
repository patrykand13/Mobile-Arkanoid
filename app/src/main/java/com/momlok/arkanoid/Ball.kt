package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.ballSize

class Ball: Sprite() {

    private var speedBallX = 0
    private var speedBallY = 0
    private var isDestroyed = false


    fun move(){
        spriteX +=speedBallX
        spriteY +=speedBallY
    }
    fun setSpeedBallX(d:Int){
        speedBallX = d
    }
    fun setSpeedBallY(d:Int){
        speedBallY = d
    }
    fun getSpeedBallX():Int{
        return speedBallX
    }
    fun getSpeedBallY():Int{
        return speedBallY
    }
    fun destroy(){
        isDestroyed = true
    }
    fun isDestroyed(): Boolean {
        return isDestroyed
    }
    fun getRect(): Rect {
        return Rect(
            spriteX- ballSize, spriteY- ballSize,
            spriteX+ ballSize, spriteY+ ballSize
        )
    }
}