package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.mainSize

class Boss {
    var bossX = 6*mainSize
    var bossY = 5*mainSize
    private var bossLife = 30
    private var isLife = false

    fun getX(): Int {
        return bossX
    }
    fun getY(): Int {
        return bossY
    }
    fun isLife(){
        isLife = true
    }
    fun life(): Boolean{
        return isLife
    }
    fun getLife(): Int {
        return bossLife
    }
    fun setLife() {
        bossLife--
        if(bossLife==0) isLife = false
    }
    fun getRect(): Rect {
        return Rect(
                bossX, bossY,
                bossX+8*mainSize, bossY+10*mainSize
        )
    }

}