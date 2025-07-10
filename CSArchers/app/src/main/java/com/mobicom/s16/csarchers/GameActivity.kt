package com.mobicom.s16.csarchers

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityGameBinding
import java.util.Locale

val baseOptions = listOf("binary", "octal", "decimal", "hex")
val baseToRadix = mapOf("binary" to 2, "octal" to 8, "decimal" to 10, "hex" to 16)

data class ConversionProblem(
    val fromBaseName: String,
    val toBaseName: String,
    val numberInFromBase: String,
    val decimalValue: Int
)

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun generateRandomConversionProblem(): ConversionProblem {
    val fromBaseName = baseOptions.random()
    var toBaseName: String

    do {
        toBaseName = baseOptions.random()
    } while (toBaseName == fromBaseName)

    val randomDecimalValue = (1..255).random() // range can be adjusted
    val numberInFromBase = Integer.toString(randomDecimalValue, baseToRadix[fromBaseName]!!)

    return ConversionProblem(
        fromBaseName,
        toBaseName,
        numberInFromBase,
        randomDecimalValue
    )
}

fun convertNumber(input: String, fromBase: Int, toBase: Int): String {
    return try {
        val decimalValue = input.toInt(fromBase)
        decimalValue.toString(toBase)
    } catch (e: NumberFormatException) {
        "Invalid input for base $fromBase"
    }
}

fun View.hopAnimation() {
    this.animate()
        .translationYBy(-50f) // move up 50 pixels
        .setDuration(150)
        .withEndAction {
            this.animate()
                .translationYBy(50f) // move back down
                .setDuration(150)
                .start()
        }
        .start()
}

fun View.wiggleAnimation() {
    val distance = 20f // pixels left/right

    this.animate()
        .translationXBy(-distance)
        .setDuration(100)
        .withEndAction {
            this.animate()
                .translationXBy(2 * distance)
                .setDuration(100)
                .withEndAction {
                    this.animate()
                        .translationXBy(-distance)
                        .setDuration(100)
                        .start()
                }
                .start()
        }
        .start()
}

fun showExplosionForMoment(explosionView: ImageView, duration: Long = 300L) {
    explosionView.visibility = View.VISIBLE

    Handler(Looper.getMainLooper()).postDelayed({
        explosionView.visibility = View.GONE
    }, duration)
}

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityGameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val drawingView = findViewById<DrawingView>(R.id.drawing_view)

        val totalTime = 60_000L  // 10 seconds
        val interval = 100L      // update every 100 ms
        val progressBar = viewBinding.timePb

        progressBar.max = (totalTime / interval).toInt()

        val timer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished / interval).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                progressBar.progress = 0  // show empty bar

                // TO DO:
                // IF TIME OUT, GET HIT, GENERATE NEW QUESTION, TIME RESET
            }
        }

        timer.start()

        val problem = generateRandomConversionProblem()

        viewBinding.frombaseTv.setText(problem.fromBaseName.capitalized()+":")
        viewBinding.frombasenumTv.setText(problem.numberInFromBase.uppercase())
        viewBinding.tobaseTv.setText(problem.toBaseName.capitalized()+":")

        viewBinding.submitBtn.setOnClickListener {
            val userInput = viewBinding.tobasenumEt.text.toString()

            val correctAnswer = convertNumber(
                problem.numberInFromBase,
                baseToRadix[problem.fromBaseName]!!,
                baseToRadix[problem.toBaseName]!!
            )

            if (userInput.lowercase() == correctAnswer.lowercase()) {
                viewBinding.playerIv.hopAnimation()
                viewBinding.enemyIv.wiggleAnimation()
                showExplosionForMoment(viewBinding.enemyHurtIV)

                // TO DO: + POINTS, GENERATE NEW QUESTION, TIMER RESET

            } else {
                viewBinding.playerIv.wiggleAnimation()
                // TO DO:
                // TIME GETS DEDUCTED
            }
        }

        // TO DO:
        // IF ENEMY DIES, CHANGE TO NEW ENEMY
        // IF PLAYER DIES, GAME OVER
    }
}