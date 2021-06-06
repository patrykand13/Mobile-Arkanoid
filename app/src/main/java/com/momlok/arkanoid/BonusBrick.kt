package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.mainSize

class BonusBrick(x: Int, y: Int,bonus: String, color: String) {
    private var isDestroyed = false
    private var brickX = x
    private var brickY = y
    private var bonusType = bonus
    private var bonusColor = color

    fun getX(): Int {
        return brickX
    }
    fun getY(): Int {
        return brickY
    }
    fun getBonusType(): String{
        return  bonusType
    }
    fun getBonusColor(): String{
        return  bonusColor
    }

    fun isDestroyed(): Boolean {
        return isDestroyed
    }
    fun move(){
        brickY +=5
    }

    fun setDestroyed() {
        isDestroyed = true
    }
    fun getRect(): Rect {
        return Rect(
            brickX, brickY,
            brickX+2*mainSize, brickY+mainSize
        )
    }
}