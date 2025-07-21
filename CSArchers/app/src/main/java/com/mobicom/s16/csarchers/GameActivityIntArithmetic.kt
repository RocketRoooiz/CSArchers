package com.mobicom.s16.csarchers

import android.app.AlertDialog
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityGameBinding
import java.nio.charset.Charset
import kotlin.random.Random
import java.util.Locale

val operations = listOf("+", "-", "*", "/")

data class BinaryProblem(
    val operand1: String,
    val operand2: String,
    val operation: String,
    val correctAnswer: String
)

fun generateRandomBinaryProblem(): BinaryProblem {
    val op = operations.random()
    var aInt: Int
    var bInt: Int

    when (op) {
        "-" -> {
            aInt = Random.nextInt(0, 256)
            bInt = Random.nextInt(0, aInt + 1) // ensures a ≥ b
        }
        "/" -> {
            aInt = Random.nextInt(0, 256)
            bInt = Random.nextInt(1, 256) // ensures b ≠ 0
        }
        else -> {
            aInt = Random.nextInt(0, 256)
            bInt = Random.nextInt(0, 256)
        }
    }

    val a = aInt.toString(2)
    val b = bInt.toString(2)

    val result = when (op) {
        "+" -> (aInt + bInt)
        "-" -> (aInt - bInt)
        "*" -> (aInt * bInt)
        "/" -> (aInt / bInt)
        else -> 0
    }.toString(2)

    return BinaryProblem(
        operand1 = a,
        operand2 = b,
        operation = op,
        correctAnswer = result
    )
}


class GameActivityIntArithmetic : ComponentActivity() {
    private lateinit var viewBinding : ActivityGameBinding
    private lateinit var drawingView: DrawingView

    private var score = 0
    private var enemyHp = 3
    private var playerHp = 3
    private var totalTime = 60_000L
    private var interval = 100L
    private lateinit var timer: CountDownTimer
    private var currentProblem: BinaryProblem? = null
    private lateinit var soundPool: SoundPool
    private var explosionSoundId: Int = 0

    private var isTiger = true

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

            if (userInput.lowercase() == problem.correctAnswer.lowercase()) {
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

                    viewBinding.enemyIv.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction {
                            // Switch the image once fade-out is done
                            if (isTiger) {
                                viewBinding.enemyIv.setImageResource(R.drawable.character_robot)
                                viewBinding.enemyCharNameTv.text = "Robot"
                            } else {
                                viewBinding.enemyIv.setImageResource(R.drawable.character_tiger)
                                viewBinding.enemyCharNameTv.text = "Tiger"
                            }
                            isTiger = !isTiger

                            viewBinding.enemyIv.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .start()
                        }
                        .start()
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
        currentProblem = generateRandomBinaryProblem()
        val problem = currentProblem!!
        Log.d("INT_ARITHMETIC_CONVERSION", "Correct answer: ${problem.correctAnswer}")
        viewBinding.frombaseTv.setText("Given:")
        viewBinding.frombasenumTv.text = "${problem.operand1} ${problem.operation} ${problem.operand2}"
        viewBinding.tobaseTv.setText("Answer:")
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