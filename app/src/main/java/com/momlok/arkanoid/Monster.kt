package com.momlok.arkanoid

import android.graphics.Rect
import com.momlok.arkanoid.Helper.Companion.mainSize

class Monster(x: Int, y: Int) {
    private var speedMonsterX = 2
    private var speedMonsterY = 2
    private var monsterX = x
    private var monsterY = y
    private var lastTime = 0
    private var isDestroyed = false

    fun getX(): Int {
        return monsterX
    }
    fun getY(): Int {
        return monsterY
    }
    fun getTime(): Int {
        return lastTime
    }
    fun setTime(time: Int){
        lastTime=time
    }
    fun isDestroyed(): Boolean {
        return isDestroyed
    }

    fun setDestroyed(state: Boolean) {
        isDestroyed = state
    }

    fun move(){
        monsterX +=speedMonsterX
        monsterY +=speedMonsterY
    }
    fun setSpeedMonsterX(d:Int){
        speedMonsterX = d
    }
    fun setSpeedMonsterY(d:Int){
        speedMonsterY = d
    }
    fun getSpeedMonsterX():Int{
        return speedMonsterX
    }
    fun getSpeedMonsterY():Int{
        return speedMonsterY
    }
    fun getRect(): Rect {
        return Rect(
                monsterX, monsterY,
                monsterX+ mainSize, monsterY+mainSize
        )
    }
}