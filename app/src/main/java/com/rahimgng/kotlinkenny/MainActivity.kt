package com.rahimgng.kotlinkenny

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var score = 0
    var imageArray = ArrayList<ImageView>()
    var handler = Handler()
    var runnable = Runnable { }
    lateinit var sharedPreferences: SharedPreferences
    var lastScore: Int? = null
    var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences =
            this.getSharedPreferences("com.rahimgng.kotlinkenny", Context.MODE_PRIVATE)

        lastScore = sharedPreferences.getInt("lastScore", 0)
        highScore = sharedPreferences.getInt("highScore", 0)

        if (lastScore == 0)
            lastScoreText.text = "Last Score :"
        else
            lastScoreText.text = "Last Score : ${lastScore}"
        //if (lastScore > score)
        if (highScore == 0)
            highScoreText.text = "High Score : 0"
        else
            highScoreText.text = "High Score : ${highScore}"


        imageArray.add(imageView)
        imageArray.add(imageView2)
        imageArray.add(imageView3)
        imageArray.add(imageView4)
        imageArray.add(imageView5)
        imageArray.add(imageView6)
        imageArray.add(imageView7)
        imageArray.add(imageView8)
        imageArray.add(imageView9)

        hideImages()

        object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeText.text = "Time : " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                timeText.text = "Time : 0"
                handler.removeCallbacks(runnable)

                for (i in imageArray)
                    i.visibility = View.INVISIBLE
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game over!")
                alert.setMessage("Restart?")
                alert.setPositiveButton("Yes") { dialog, which ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(this@MainActivity, "Game Over!", Toast.LENGTH_LONG).show()
                }
                alert.show()
                lastScoreText.text = "Last Score : ${lastScore}"
                highScoreText.text = "High Score : ${highScore}"
            }

        }.start()
    }

    fun hideImages() {
        runnable = object : Runnable {
            override fun run() {
                for (i in imageArray) {
                    i.visibility = View.INVISIBLE
                }
                val random = Random()
                val randomIndex = random.nextInt(9)
                imageArray[randomIndex].visibility = View.VISIBLE

                handler.postDelayed(runnable, 500)
            }
        }
        handler.post(runnable)
    }

    fun increaseScore(view: View) {
        score += 1

        scoreText.text = "Score : $score"
        sharedPreferences.edit().putInt("lastScore", score).apply()
        if(lastScore!! > highScore)
            sharedPreferences.edit().putInt("highScore", lastScore!!).apply()
    }

    fun restart(view: View) {
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Game will Restart")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("Yes") { dialog, which ->
            val intent = intent
            finish()
            startActivity(intent)
        }
        alert.setNegativeButton("No") { dialog, which ->
            Toast.makeText(this@MainActivity, "Cancel", Toast.LENGTH_SHORT).show()
        }
        alert.show()
        lastScoreText.text = "Score : ${lastScore}"
        highScoreText.text = "High Score : ${highScore}"
    }

    fun quitApp(view: View) {
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Exiting App")
        alert.setMessage("R u sure?")
        alert.setPositiveButton("Yes") { dialog, which ->
            finishAffinity()
        }
        alert.setNegativeButton("No") { dialog, which ->
            Toast.makeText(this@MainActivity, "Canceled!", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }
}