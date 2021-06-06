package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.mainSize


class Brick(x: Int, y: Int,life: Int, canDestroyed: Boolean) {
    private var isDestroyed = false
    private var brickX = x
    private var brickY = y
    private var brickLife = life
    private var brickCanDestroyed = canDestroyed

    fun getX(): Int {
        return brickX
    }
    fun getY(): Int {
        return brickY
    }
    fun canDestroyed(): Boolean {
        return brickCanDestroyed
    }
    fun getLife(): Int {
        return brickLife
    }
    fun setLife() {
        if(brickCanDestroyed){
            brickLife--
            if(brickLife==0) isDestroyed = true
        }
    }

    fun isDestroyed(): Boolean {
        return isDestroyed
    }

    fun getRect(): Rect {
        return Rect(
            brickX, brickY,
                (brickX+2.8*mainSize).toInt(), (brickY+1.8*mainSize).toInt()
        )
    }
}