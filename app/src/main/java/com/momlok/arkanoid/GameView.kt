package com.momlok.arkanoid

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.contains
import com.momlok.arkanoid.Helper.Companion.ballSize
import com.momlok.arkanoid.Helper.Companion.paddleColor
import com.momlok.arkanoid.Helper.Companion.mainSize
import kotlin.random.Random


class GameView(context: Context, attrs: AttributeSet? = null) : View(context) {
    private var paint = Paint()
    private var myWidth = 0
    private var myHeight = 0
    private var game = true
    private var startGame = false
    private val bricks: MutableList<Brick> = ArrayList()
    private val bonusBricks: MutableList<BonusBrick> = ArrayList()
    private val monsters: MutableList<Monster> = ArrayList()
    private val bossBall: MutableList<Ball> = ArrayList()
    private var ball = Ball()
    private var paddle = Paddle()
    private var boss = Boss()
    private var life = 3
    private var shoot = false
    private var lastPointMonster = 0
    private var random = Random.nextInt(1,15)
    private var points = 0
    private var createMonster = 0
    private var big =0
    private var slowBall = 0
    private var nextLevel = false
    private var level = 1
    private var destroyBrick=0
    private var maxLevel = 4
    private var noDestroy=0
    private var bossTimer = 0
    private var hitMedia = MediaPlayer.create(context, R.raw.hit)
    private var boomMedia = MediaPlayer.create(context, R.raw.boom)
    private var bossMedia = MediaPlayer.create(context, R.raw.boss)
    private var loseMedia = MediaPlayer.create(context, R.raw.lose)
    private var winMedia = MediaPlayer.create(context, R.raw.win)

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        fun start(){
            mainSize = width/20
            ballSize = mainSize/5
            destroyBrick = 0
            ball.setX(width / 2)
            ball.setY((height - 2.5*mainSize).toInt())
            paddle.setX(width / 2 - 2*mainSize)
            paddle.setY(height - 2*mainSize)
            myWidth = width
            myHeight = height
            when (level) {
                1 -> {
                    monsters.clear()
                    for (i in 0 until 6){
                        for(j in 0 until 4){
                            if (j==3) bricks.add(Brick(i * 3*mainSize +mainSize, j * 2*mainSize + 2*mainSize,2,true))
                            else bricks.add(Brick(i * 3*mainSize + mainSize, j * 2*mainSize + 2*mainSize,1,true))
                        }

                    }
                }
                2 -> {
                    monsters.clear()
                    var col = 6
                    for (i in 6 downTo 0){
                        for(j in 0 until col){
                            if(i==6&& j<5) {
                                bricks.add(Brick(j * 3*mainSize + mainSize, i * 2*mainSize + 2*mainSize,0,false))
                                noDestroy++
                            }
                            else bricks.add(Brick(j * 3*mainSize + mainSize, i * 2*mainSize + 2*mainSize,3,true))
                        }
                        col--
                    }
                }
                3 -> {
                    monsters.clear()
                    boss = Boss()
                    boss.isLife()
                }
            }
            startGame = true
        }
        fun endGame(){
            winMedia.start()
            paint.textSize = mainSize.toFloat()
            paint.color = Color.RED
            game = false
            bossTimer=0
            canvas?.drawText(
                "Congratulations this is END GAME",
                (width / 2 - 7*mainSize).toFloat(),
                (height / 2).toFloat(),
                paint
            )
            canvas?.drawText(
                "Your score: $points",
                (width / 2 - 3*mainSize).toFloat(),
                (height / 2 + 2*mainSize).toFloat(),
                paint
            )
            canvas?.drawText(
                "Click back and go to menu",
                (width / 2 - 6*mainSize).toFloat(),
                (height / 2 + 4*mainSize).toFloat(),
                paint
            )
        }
        if(!game){

            if(level>=maxLevel){
                endGame()
            }else{
                if(life>0){
                    paddle.setX(width / 2 - 2*mainSize)
                    ball.setX(width / 2)
                    ball.setY((height - 2.5*mainSize).toInt())
                    ball.setSpeedBallX(0)
                    ball.setSpeedBallY(0)
                    paddle.setSpeed(0)
                    life--
                    shoot = false
                    game = true
                }else{
                    loseMedia.start()
                    ball.setSpeedBallX(0)
                    ball.setSpeedBallY(0)
                    paddle.setSpeed(0)
                    paint.textSize = mainSize.toFloat()
                    paint.color = Color.RED
                    bossTimer=0
                    canvas?.drawText(
                        "GAME OVER",
                        (width / 2 - 3*mainSize).toFloat(),
                        (height / 2).toFloat(),
                        paint
                    )
                    canvas?.drawText(
                        "Your score: $points",
                        (width / 2 - 3*mainSize).toFloat(),
                        (height / 2 + 2*mainSize).toFloat(),
                        paint
                    )
                    canvas?.drawText(
                        "Click back and go to menu",
                        (width / 2 - 6*mainSize).toFloat(),
                        (height / 2 + 4*mainSize).toFloat(),
                        paint
                    )
                }
            }

        }
        if(!startGame) {
            start()
        }
        if(level>=maxLevel){
            bricks.clear()
            game = false
        }

        if(destroyBrick>=bricks.size-noDestroy && !boss.life()){
            if(level <maxLevel) level++
            bricks.clear()
            ball.setX(paddle.getX()+2*mainSize)
            ball.setY((height - 2.5*mainSize).toInt())
            ball.setSpeedBallX(0)
            ball.setSpeedBallY(0)
            shoot=false
            startGame=false
        }
        if(random+lastPointMonster==points){
            monsters.add(Monster(2*mainSize,mainSize))
            createMonster =20
            lastPointMonster = points
            random = Random.nextInt(1,15)
        }
        if(slowBall>1){
            slowBall--
        }else if (slowBall== 1){
            if (ball.getSpeedBallX()>0) ball.setSpeedBallX(ball.getSpeedBallX()+3)
            else ball.setSpeedBallX(ball.getSpeedBallX()-3)
            if (ball.getSpeedBallY()>0) ball.setSpeedBallY(ball.getSpeedBallY()+3)
            else ball.setSpeedBallY(ball.getSpeedBallY()-3)
            slowBall--
        }
        //draw nextLevel exit
        if(nextLevel){
            paint.color = Color.BLUE
            canvas?.drawRect(
                myWidth-(0.4*mainSize).toFloat(),
                myHeight - (2.6*mainSize).toFloat(),
                myWidth.toFloat(),
                myHeight - (0.2*mainSize).toFloat(),
                paint
            )
        }
        //draw points and level
        paint.textSize = mainSize.toFloat()
        paint.color = Color.RED
        canvas?.drawText(
            "Points: $points",
            (2* ballSize).toFloat(),
            (5* ballSize).toFloat(),
            paint
        )
        canvas?.drawText(
            "Level: $level",
            (myWidth-4*mainSize).toFloat(),
            (5* ballSize).toFloat(),
            paint
        )
        //draw life
        for (i in 0 until life){
            paint.color = Color.parseColor(paddleColor)
            canvas?.drawRect(
                i * 2*mainSize + 2*ballSize.toFloat(),
                myHeight - 4*ballSize.toFloat(),
                i * 2*mainSize + 2*mainSize.toFloat(),
                myHeight - 2*ballSize.toFloat(),
                paint
            )
        }
        if(big<=0){
            paddle.setBig(false)
        }
        //draw paddle
        if(paddle.isBig()){
            paint.color = Color.parseColor(paddleColor)
            canvas?.drawRect(
                paddle.getX().toFloat(),
                paddle.getY().toFloat(),
                paddle.getX() + 8*mainSize.toFloat(),
                paddle.getY() + mainSize.toFloat(),
                paint
            )
            big--
        }else {
            paint.color = Color.parseColor(paddleColor)
            canvas?.drawRect(
                paddle.getX().toFloat(),
                paddle.getY().toFloat(),
                paddle.getX() + 5*mainSize.toFloat(),
                paddle.getY() + mainSize.toFloat(),
                paint
            )
        }
        //draw ball
        canvas?.drawCircle(ball.getX().toFloat(), ball.getY().toFloat(), 2*ballSize.toFloat(), paint)
        //draw bricks
        for (i in 0 until bricks.size) {
            if (!bricks[i].isDestroyed()){
                if(bricks[i].getLife()==1) paint.color = Color.BLUE
                else if(bricks[i].getLife()==2) paint.color = Color.GREEN
                else if(bricks[i].getLife()==3) paint.color = Color.BLACK
                else if(!bricks[i].canDestroyed()) paint.color = Color.GRAY
                canvas?.drawRect(
                    bricks[i].getX().toFloat(),
                    bricks[i].getY().toFloat(),
                    bricks[i].getX() + (2.8*mainSize).toFloat(),
                    bricks[i].getY() + (1.8*mainSize).toFloat(), paint
                )
            }
        }
        if(createMonster>0){
            paint.color = Color.GREEN
            canvas?.drawCircle(2*mainSize.toFloat(), (1.3*mainSize).toFloat(), mainSize.toFloat()+createMonster, paint)
            createMonster--
        }
        //draw bonusBricks
        for (i in 0 until bonusBricks.size){
            if (!bonusBricks[i].isDestroyed()){
                val color = bonusBricks[i].getBonusColor()
                paint.color = Color.parseColor(color)
                canvas?.drawRect(
                    bonusBricks[i].getX().toFloat(),
                    bonusBricks[i].getY().toFloat(),
                    bonusBricks[i].getX() + 2*mainSize.toFloat(),
                    bonusBricks[i].getY() + mainSize.toFloat(), paint
                )
            }
        }
        //draw Boss
        if(level ==3){
            if(boss.life()) {
                when {
                    boss.getLife()>20 -> paint.color = Color.YELLOW
                    boss.getLife()>10 -> paint.color = Color.RED
                    boss.getLife()>0 -> paint.color = Color.DKGRAY
                }
                //body
                canvas?.drawRect(
                        boss.getX().toFloat(),
                        boss.getY().toFloat(),
                        boss.getX() + 8*mainSize.toFloat(),
                        boss.getY() + 10*mainSize.toFloat(), paint
                )
                paint.color = Color.GREEN
                //eyes
                canvas?.drawRect(
                        boss.getX()+ mainSize.toFloat(),
                        boss.getY()+ mainSize.toFloat(),
                        boss.getX() + 3*mainSize.toFloat(),
                        boss.getY() + 2*mainSize.toFloat(), paint
                )
                canvas?.drawRect(
                        boss.getX()+ 5*mainSize.toFloat(),
                        boss.getY()+ mainSize.toFloat(),
                        boss.getX() + 7*mainSize.toFloat(),
                        boss.getY() + 2*mainSize.toFloat(), paint
                )
                //mouth
                canvas?.drawCircle(
                        boss.getX()+ 4*mainSize.toFloat(),
                        boss.getY()+ 7*mainSize.toFloat(),
                        mainSize.toFloat(), paint
                )
                //tears
                paint.color = Color.BLACK
                canvas?.drawRect(
                        (boss.getX()+ 2.8*mainSize.toFloat()).toFloat(),
                        (boss.getY()+ 2.3*mainSize.toFloat()).toFloat(),
                        boss.getX() + 3*mainSize.toFloat(),
                        (boss.getY() + 3.5*mainSize.toFloat()).toFloat(), paint
                )
                canvas?.drawRect(
                        (boss.getX()+ 2.8*mainSize.toFloat()).toFloat(),
                        boss.getY()+ 4*mainSize.toFloat(),
                        boss.getX() + 3*mainSize.toFloat(),
                        (boss.getY() + 5.3*mainSize.toFloat()).toFloat(), paint
                )
                ballBossCheck()
            }
        }

        ballWallCheck()
        ballPaddleCheck()
        ballBrickCheck()
        ballMonsterCheck()
        monsterWallCheck()
        paddleWallCheck()
        bonusBricksCheck()
        bossBallWallCheck()
        bossBallPaddleCheck()

        //boss ball
        if(bossTimer==1){
            when {
                boss.getLife()>20 -> bossTimer =500
                boss.getLife()>10 -> bossTimer =300
                boss.getLife()>0 -> bossTimer =100
            }
            bossBall.add(Ball())
            val size = bossBall.size-1
            bossBall[size].setX(boss.getX()+4* mainSize)
            bossBall[size].setY(boss.getY()+7*mainSize)
            val r = Random.nextInt(-5,5)
            bossBall[size].setSpeedBallX(r)
            bossBall[size].setSpeedBallY(3)

        }
        for (i in 0 until bossBall.size){
            if(!bossBall[i].isDestroyed()){
                paint.color = Color.CYAN
                canvas?.drawCircle(bossBall[i].getX().toFloat(), bossBall[i].getY().toFloat(), 2* ballSize.toFloat(), paint)
                bossBall[i].move()
            }
        }

        //draw monsters
        for (i in 0 until monsters.size){
            paint.color = Color.YELLOW
            if (!monsters[i].isDestroyed()){
                canvas?.drawRect(
                    monsters[i].getX().toFloat(),
                    monsters[i].getY().toFloat(),
                    monsters[i].getX() + mainSize.toFloat(),
                    monsters[i].getY().toFloat()+mainSize, paint
                )
                paint.color = Color.RED
                canvas?.drawRect(
                        monsters[i].getX()+ ballSize.toFloat(),
                        monsters[i].getY()+ ballSize.toFloat(),
                        monsters[i].getX()+ 2*ballSize.toFloat(),
                        monsters[i].getY()+ 2*+ ballSize.toFloat(), paint
                )
                canvas?.drawRect(
                        monsters[i].getX()- 2*ballSize + mainSize.toFloat(),
                        monsters[i].getY()+ ballSize.toFloat(),
                        monsters[i].getX()- ballSize + mainSize.toFloat(),
                        monsters[i].getY()+ 2*+ ballSize.toFloat(), paint
                )
                //change move monsters
                if (monsters[i].getTime()==100){
                    when (Random.nextInt(0,5)) {
                        0 -> {
                            monsters[i].setSpeedMonsterX(-monsters[i].getSpeedMonsterX())
                            monsters[i].setSpeedMonsterY(-monsters[i].getSpeedMonsterY())
                        }
                        1 -> {
                            monsters[i].setSpeedMonsterY(-monsters[i].getSpeedMonsterY())
                        }
                        2 -> {
                            monsters[i].setSpeedMonsterX(-monsters[i].getSpeedMonsterX())
                        }
                    }

                    monsters[i].setTime(0)
                }
                monsters[i].move()
                monsters[i].setTime(monsters[i].getTime()+1)
            }
        }

        for (i in 0 until bonusBricks.size){
            if (!bonusBricks[i].isDestroyed()){
                bonusBricks[i].move()
            }
        }
        ball.move()
        paddle.move()

        bossTimer--
        invalidate()
    }

    private fun ballBossCheck(){
        if((ball.getRect()).intersect(boss.getRect())) {
            hitMedia.start()
            val ballMinX = ball.getX() - ballSize
            val ballMinY = ball.getY() - ballSize

            val pointRight = Point(ballMinX + 2*ballSize, ballMinY + ballSize)
            val pointRightTop = Point(ballMinX + 2*ballSize, (ballMinY + 0.7*ballSize).toInt())
            val pointRightBottom = Point(ballMinX + 2*ballSize, (ballMinY + 1.3*ballSize).toInt())
            val pointLeft = Point(ballMinX, ballMinY + ballSize)
            val pointLeftTop = Point(ballMinX, (ballMinY + 0.7*ballSize).toInt())
            val pointLeftBottom = Point(ballMinX, (ballMinY + 1.3*ballSize).toInt())
            val pointTop = Point(ballMinX + ballSize, ballMinY)
            val pointTopLeft = Point((ballMinX + 0.7*ballSize).toInt(), ballMinY)
            val pointTopRight = Point((ballMinX + 1.3*ballSize).toInt(), ballMinY)
            val pointBottom = Point(ballMinX + ballSize, ballMinY + 2*ballSize)
            val pointBottomLeft = Point((ballMinX + 0.7*ballSize).toInt(), ballMinY + 2*ballSize)
            val pointBottomRight = Point((ballMinX + 1.3*ballSize).toInt(), ballMinY + 2*ballSize)
            if (boss.life()) {

                if (boss.getRect().contains(pointTop)
                        || boss.getRect().contains(pointTopLeft)
                        || boss.getRect().contains(pointTopRight)) {
                    ball.setSpeedBallY(-ball.getSpeedBallY())
                } else if (boss.getRect().contains(pointBottom)
                        || boss.getRect().contains(pointBottomLeft)
                        || boss.getRect().contains(pointBottomRight)) {
                    ball.setSpeedBallY(-ball.getSpeedBallY())
                } else if (boss.getRect().contains(pointRight)
                        || boss.getRect().contains(pointRightTop)
                        || boss.getRect().contains(pointRightBottom)) {
                    ball.setSpeedBallX(-ball.getSpeedBallX())
                } else if (boss.getRect().contains(pointLeft)
                        || boss.getRect().contains(pointLeftTop)
                        || boss.getRect().contains(pointLeftBottom)) {
                    ball.setSpeedBallX(-ball.getSpeedBallX())
                }

                //bricks[i].setDestroyed(true)
                boss.setLife()
                if(boss.getLife()==20 || boss.getLife()==10) bossMedia.start()
            }
        }
    }

    private fun ballWallCheck(){
        if(ball.getX()<=0) {
            hitMedia.start()
            ball.setSpeedBallX(-ball.getSpeedBallX())
        }
        if (ball.getX()>=myWidth-20) {
            hitMedia.start()
            ball.setSpeedBallX(-ball.getSpeedBallX())
        }
        if (ball.getY()<=0) {
            hitMedia.start()
            ball.setSpeedBallY(-ball.getSpeedBallY())
        }
        if(ball.getY()>=myHeight-20) game = false

    }
    private fun bossBallWallCheck(){
        for(i in 0 until bossBall.size){
            if(!bossBall[i].isDestroyed()){
                if(bossBall[i].getX()<=0) bossBall[i].setSpeedBallX(-bossBall[i].getSpeedBallX())
                if (bossBall[i].getX()>=myWidth-2*ballSize) bossBall[i].setSpeedBallX(-bossBall[i].getSpeedBallX())
                if (bossBall[i].getY()<=0) bossBall[i].setSpeedBallY(-bossBall[i].getSpeedBallY())
                if(bossBall[i].getY()>=myHeight-2*ballSize) bossBall[i].destroy()
            }
        }
    }
    private fun bossBallPaddleCheck(){
        for(i in 0 until bossBall.size) {
            if (!bossBall[i].isDestroyed()&&(bossBall[i].getRect()).intersect(paddle.getRect())) {
                bossBall[i].destroy()
                game = false
            }
        }
    }
    private fun ballPaddleCheck(){
        if((ball.getRect()).intersect(paddle.getRect())){
            hitMedia.start()
            ball.setSpeedBallY(-ball.getSpeedBallY())
        }
    }
    private fun ballBrickCheck(){
        for (i in 0 until bricks.size){
            if((ball.getRect()).intersect(bricks[i].getRect())){

                val ballMinX = ball.getX()-ballSize
                val ballMinY = ball.getY()-ballSize
                val randomBonus = Random.nextInt(1,100)

                val pointRight = Point(ballMinX + 2*ballSize, ballMinY + ballSize)
                val pointRightTop = Point(ballMinX + 2*ballSize, (ballMinY + 0.7*ballSize).toInt())
                val pointRightBottom = Point(ballMinX + 2*ballSize, (ballMinY + 1.3*ballSize).toInt())
                val pointLeft = Point(ballMinX, ballMinY + ballSize)
                val pointLeftTop = Point(ballMinX, (ballMinY + 0.7*ballSize).toInt())
                val pointLeftBottom = Point(ballMinX, (ballMinY + 1.3*ballSize).toInt())
                val pointTop = Point(ballMinX + ballSize, ballMinY)
                val pointTopLeft = Point((ballMinX + 0.7*ballSize).toInt(), ballMinY)
                val pointTopRight = Point((ballMinX + 1.3*ballSize).toInt(), ballMinY)
                val pointBottom = Point(ballMinX + ballSize, ballMinY + 2*ballSize)
                val pointBottomLeft = Point((ballMinX + 0.7*ballSize).toInt(), ballMinY + 2*ballSize)
                val pointBottomRight = Point((ballMinX + 1.3*ballSize).toInt(), ballMinY + 2*ballSize)
                if (!bricks[i].isDestroyed()) {
                    hitMedia.start()
                    if (bricks[i].getRect().contains(pointTop)
                        ||bricks[i].getRect().contains(pointTopLeft)
                        ||bricks[i].getRect().contains(pointTopRight)) {
                        ball.setSpeedBallY(-ball.getSpeedBallY())
                    }else if (bricks[i].getRect().contains(pointBottom)
                        ||bricks[i].getRect().contains(pointBottomLeft)
                        ||bricks[i].getRect().contains(pointBottomRight)) {
                        ball.setSpeedBallY(-ball.getSpeedBallY())
                    }else if (bricks[i].getRect().contains(pointRight)
                        ||bricks[i].getRect().contains(pointRightTop)
                        ||bricks[i].getRect().contains(pointRightBottom)) {
                        ball.setSpeedBallX(-ball.getSpeedBallX())
                    }else if (bricks[i].getRect().contains(pointLeft)
                        ||bricks[i].getRect().contains(pointLeftTop)
                        ||bricks[i].getRect().contains(pointLeftBottom)) {
                        ball.setSpeedBallX(-ball.getSpeedBallX())
                    }

                    //bricks[i].setDestroyed(true)
                    bricks[i].setLife()
                    if(bricks[i].isDestroyed()){
                        points++
                        destroyBrick++
                        when (randomBonus) {
                            1 -> {
                                bonusBricks.add(BonusBrick(ball.getX(),ball.getY(),"B","#a452f7"))
                            }
                            in 2..5 -> {
                                bonusBricks.add(BonusBrick(ball.getX(),ball.getY(),"S","#548053"))
                            }
                            in 6..11 -> {
                                bonusBricks.add(BonusBrick(ball.getX(),ball.getY(),"E", "#070b8c"))
                            }
                            in 12..19 -> {
                                bonusBricks.add(BonusBrick(ball.getX(),ball.getY(),"P","#ff0000"))
                            }
                            in 20..30 -> {
                                bonusBricks.add(BonusBrick(ball.getX(),ball.getY(),"C","#634b2d"))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ballMonsterCheck(){
        for (i in 0 until monsters.size){
            if(!monsters[i].isDestroyed()){
                if((ball.getRect()).intersect(monsters[i].getRect())){
                    boomMedia.start()
                    if (ball.getSpeedBallX()>0) ball.setSpeedBallX(ball.getSpeedBallX()+2)
                    else ball.setSpeedBallX(ball.getSpeedBallX()-2)
                    if (ball.getSpeedBallY()>0) ball.setSpeedBallY(ball.getSpeedBallY()+2)
                    else ball.setSpeedBallY(ball.getSpeedBallY()-2)
                    monsters[i].setDestroyed(true)
                }
            }
        }
    }
    private fun monsterWallCheck(){
        for (i in 0 until monsters.size){
            if(monsters[i].getX()<=0) monsters[i].setSpeedMonsterX(-monsters[i].getSpeedMonsterX())
            if (monsters[i].getX()>=myWidth-50) monsters[i].setSpeedMonsterX(-monsters[i].getSpeedMonsterX())
            if (monsters[i].getY()<=0) monsters[i].setSpeedMonsterY(-monsters[i].getSpeedMonsterY())
            if(monsters[i].getY()>=myHeight-20) monsters[i].setDestroyed(true)
        }

    }

    private fun paddleWallCheck(){
        if(paddle.getX()<=0 ) paddle.setX(0)
        if(paddle.getX()+250>=myWidth){
            if(nextLevel){
                level++
                bricks.clear()
                ball.setX(paddle.getX()+2*mainSize)
                ball.setY((height - 2.5*mainSize).toInt())
                ball.setSpeedBallX(0)
                ball.setSpeedBallY(0)
                shoot=false
                startGame=false
                nextLevel = false
            }else paddle.setX(myWidth - 5*mainSize)
        }

    }

    private fun bonusBricksCheck(){
        for (i in 0 until bonusBricks.size){
            if(!bonusBricks[i].isDestroyed()){
                if(bonusBricks[i].getRect().intersect(paddle.getRect())){
                    val bonusType = bonusBricks[i].getBonusType()
                    if(bonusType == "P") life++
                    if(bonusType == "C") {
                        ball.setX(paddle.getX()+2*mainSize)
                        ball.setY((height - 2.5*mainSize).toInt())
                        ball.setSpeedBallX(0)
                        ball.setSpeedBallY(0)
                        shoot=false
                    }
                    if(bonusType == "E") {
                        paddle.setBig(true)
                        big=500
                    }
                    if(bonusType == "S" && slowBall==0) {
                        if (ball.getSpeedBallX()>0) ball.setSpeedBallX(ball.getSpeedBallX()-3)
                        else ball.setSpeedBallX(ball.getSpeedBallX()+3)
                        if (ball.getSpeedBallY()>0) ball.setSpeedBallY(ball.getSpeedBallY()-3)
                        else ball.setSpeedBallY(ball.getSpeedBallY()+3)
                        slowBall = 300
                    }
                    if(bonusType == "B") {
                        nextLevel = true
                    }
                    bonusBricks[i].setDestroyed()
                }
                if (bonusBricks[i].getY()>myHeight-20){
                    bonusBricks[i].setDestroyed()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < width / 2) {
                    paddle.setSpeed(-10)
                    if (!shoot) {
                        ball.setSpeedBallX(-10)
                    }
                }
                if (event.x > width / 2) {
                    paddle.setSpeed(10)
                    if (!shoot) {
                        ball.setSpeedBallX(10)
                    }
                }
                if (!shoot) {
                    if (event.y > height - 4*mainSize && event.x > width / 2) {
                        ball.setSpeedBallX(5)
                        ball.setSpeedBallY(-5)
                        if(level ==3) bossTimer =500
                        shoot = true
                    }
                    if (event.y > height - 4*mainSize && event.x < width / 2) {
                        ball.setSpeedBallX(-5)
                        ball.setSpeedBallY(-5)
                        if(level ==3) bossTimer =500
                        shoot = true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                paddle.setSpeed(0)
                if (!shoot) {
                    ball.setSpeedBallX(0)
                }
            }
        }
        return true
    }
}