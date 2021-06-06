package com.momlok.arkanoid

import com.momlok.arkanoid.Helper.Companion.mainSize

open class Sprite {
    var spriteX = mainSize
    var spriteY = mainSize
    fun getX(): Int {
        return spriteX
    }
    fun getY(): Int {
        return spriteY
    }
    fun setX(x: Int){
        spriteX = x
    }
    fun setY(y: Int){
        spriteY = y
    }
}