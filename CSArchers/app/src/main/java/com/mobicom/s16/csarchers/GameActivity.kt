package com.mobicom.s16.csarchers

import android.app.AlertDialog
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
    this.animate().cancel()
    this.translationY = 0f

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

    this.animate().cancel()
    this.translationX = 0f

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
    private lateinit var viewBinding : ActivityGameBinding
    private var score = 0
    private var enemyHp = 3
    private var playerHp = 3
    private var totalTime = 60_000L
    private var interval = 100L
    private lateinit var timer: CountDownTimer
    private var currentProblem: ConversionProblem? = null
    private lateinit var soundPool: SoundPool
    private var explosionSoundId: Int = 0
    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        drawingView = findViewById(R.id.drawing_view)

        resetGame()

        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        explosionSoundId = soundPool.load(this, R.raw.attack_sfx, 1)



        viewBinding.submitBtn.setOnClickListener {
            val userInput = viewBinding.tobasenumEt.text.toString()
            val problem = currentProblem!!

            val correctAnswer = convertNumber(
                problem.numberInFromBase,
                baseToRadix[problem.fromBaseName]!!,
                baseToRadix[problem.toBaseName]!!
            )

            if (userInput.lowercase() == correctAnswer.lowercase()) {
                viewBinding.playerIv.hopAnimation()
                viewBinding.enemyIv.wiggleAnimation()
                showExplosionForMoment(viewBinding.enemyHurtIV)
                soundPool.play(explosionSoundId, 1f, 1f, 0, 0, 1f)

                updateScore(viewBinding, 50)
                enemyHp -= 1
                viewBinding.enemyHealthPb.progress = enemyHp

                if (enemyHp <= 0) {
                    enemyHp = 3
                    viewBinding.enemyHealthPb.progress = enemyHp
                    // Optional: change enemy sprite here
                }
                updateProblem(viewBinding)
                resetTimer(viewBinding)

            } else {
                viewBinding.playerIv.wiggleAnimation()
                deductTime(10_000L, viewBinding)
            }
        }
    }

    private fun resetGame() {
        viewBinding.playerHealthPb.max = 3
        viewBinding.enemyHealthPb.max = 3

        playerHp = 3
        enemyHp = 3

        viewBinding.playerHealthPb.progress = playerHp
        viewBinding.enemyHealthPb.progress = enemyHp

        score = 0
        viewBinding.gamescoreValueTv.text = "0"

        updateProblem(viewBinding)

        if (::timer.isInitialized) {
            timer.cancel()
        }
        startTimer(viewBinding)
    }

    private fun updateProblem(viewBinding: ActivityGameBinding) {
        currentProblem = generateRandomConversionProblem()
        val problem = currentProblem!!
        viewBinding.frombaseTv.setText(problem.fromBaseName.capitalized()+":")
        viewBinding.frombasenumTv.setText(problem.numberInFromBase.uppercase())
        viewBinding.tobaseTv.setText(problem.toBaseName.capitalized()+":")
        viewBinding.tobasenumEt.text.clear()
        drawingView.clearDrawing()
    }

    private fun updateScore(viewBinding: ActivityGameBinding, points: Int) {
        score += points
        viewBinding.gamescoreValueTv.text = score.toString()
    }

    private fun startTimer(viewBinding: ActivityGameBinding) {
        val progressBar = viewBinding.timePb
        progressBar.max = (totalTime / interval).toInt()
        timer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (millisUntilFinished / interval).toInt()
            }
            override fun onFinish() {
                progressBar.progress = 0
                playerHp -= 1
                viewBinding.playerHealthPb.progress = playerHp
                viewBinding.enemyIv.hopAnimation()
                viewBinding.playerIv.wiggleAnimation()
                showExplosionForMoment(viewBinding.playerHurtIV)
                soundPool.play(explosionSoundId, 1f, 1f, 0, 0, 1f)

                if (playerHp <= 0) {
                    showGameOverDialog(score)
                } else {
                    updateProblem(viewBinding)
                    startTimer(viewBinding)
                }
            }
        }
        timer.start()
    }

    private fun resetTimer(viewBinding: ActivityGameBinding) {
        timer.cancel()
        startTimer(viewBinding)
    }

    private fun deductTime(millis: Long, viewBinding: ActivityGameBinding) {
        timer.cancel()
        val remainingTime = viewBinding.timePb.progress * interval
        val newTime = (remainingTime - millis).coerceAtLeast(0)

        timer = object : CountDownTimer(newTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                viewBinding.timePb.progress = (millisUntilFinished / interval).toInt()
            }
            override fun onFinish() {
                viewBinding.timePb.progress = 0
                playerHp -= 1
                viewBinding.playerHealthPb.progress = playerHp
                viewBinding.enemyIv.hopAnimation()
                viewBinding.playerIv.wiggleAnimation()
                showExplosionForMoment(viewBinding.playerHurtIV)
                soundPool.play(explosionSoundId, 1f, 1f, 0, 0, 1f)
                if (playerHp <= 0) {
                    showGameOverDialog(score)
                } else {
                    updateProblem(viewBinding)
                    startTimer(viewBinding)
                }
            }
        }
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun showGameOverDialog(score: Int) {
        val dialogView = layoutInflater.inflate(R.layout.activity_game_over, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<TextView>(R.id.score_Tv).text = "Score: $score"

        dialogView.findViewById<Button>(R.id.retry_Btn).setOnClickListener {
            dialog.dismiss()
            resetGame()
        }

        dialogView.findViewById<Button>(R.id.exit_Btn).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()

        val scale = resources.displayMetrics.density
        val widthInPx = (400 * scale).toInt()
        val heightInPx = (200 * scale).toInt()

        dialog.window?.setLayout(widthInPx, heightInPx)
    }
}