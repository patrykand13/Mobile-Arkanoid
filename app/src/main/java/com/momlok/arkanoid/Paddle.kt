package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.mainSize

class Paddle: Sprite() {
    private var speedPaddle = 0
    private var checkBig = false


    fun move(){
        spriteX +=speedPaddle
    }
    fun setSpeed(speed: Int){
        speedPaddle = speed
    }
    fun isBig(): Boolean{
        return checkBig
    }
    fun setBig(bool: Boolean){
        checkBig = bool
    }
    fun getRect(): Rect {
        return if(checkBig){
            Rect(
                    spriteX, spriteY,
                    spriteX+8*mainSize, spriteY+mainSize
            )
        }else Rect(
                spriteX, spriteY,
                spriteX+5*mainSize, spriteY+mainSize
        )
    }
}