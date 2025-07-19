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
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.Locale

val directionOption = listOf("unicodeToUtf", "utfToUnicode+8")
val UTFOptions = listOf("utf+8", "utf+16", "utf+32")

data class UTFConversionProblem(
    val direction: String, // "unicodeToUtf" or "utfToUnicode"
    val fromBaseName: String,
    val toBaseName: String,
    val displayValue: String, // What’s shown to the user
    val correctAnswer: String // What they must type
)

fun getRandomUnicodeCodePoint(): Int {
    val validRanges = listOf(
        0x0020..0x007E,
        0x00A0..0x00FF,
        0x0370..0x03FF,
        0x0400..0x04FF,
        0x1F600..0x1F64F
    )
    val range = validRanges.random()
    return range.random()
}

fun generateRandomUTFConversionProblem(): UTFConversionProblem {
    val direction = directionOption.random()
    val fromBaseName: String
    val toBaseName: String

    val codePoint = getRandomUnicodeCodePoint()
    val charStr = String(Character.toChars(codePoint))

    // Map to valid Java charset names with big-endian explicitly
    fun getCharsetName(option: String): String = when (option.lowercase()) {
        "utf+8" -> "UTF-8"
        "utf+16" -> "UTF-16BE"
        "utf+32" -> "UTF-32BE"
        else -> throw IllegalArgumentException("Unsupported UTF type: $option")
    }

    return if (direction == "unicodeToUtf") {
        fromBaseName = "unicode"
        toBaseName = UTFOptions.random()

        val charsetName = getCharsetName(toBaseName)
        val encodedBytes = charStr.toByteArray(Charset.forName(charsetName))

        UTFConversionProblem(
            direction = direction,
            fromBaseName = fromBaseName,
            toBaseName = toBaseName,
            displayValue = "U+${codePoint.toString(16).uppercase()}",
            correctAnswer = encodedBytes.joinToString("") {
                it.toUByte().toString(16).uppercase().padStart(2, '0')
            }
        )
    } else {
        fromBaseName = UTFOptions.random()
        toBaseName = "unicode U+"

        val charsetName = getCharsetName(fromBaseName)
        val encodedBytes = charStr.toByteArray(Charset.forName(charsetName))

        UTFConversionProblem(
            direction = direction,
            fromBaseName = fromBaseName,
            toBaseName = toBaseName,
            displayValue = encodedBytes.joinToString("") {
                it.toUByte().toString(16).uppercase().padStart(2, '0')
            },
            correctAnswer = "${codePoint.toString(16).uppercase()}"
        )
    }
}


class GameActivityUTF : ComponentActivity() {
    private lateinit var viewBinding : ActivityGameBinding
    private lateinit var drawingView: DrawingView

    private var score = 0
    private var enemyHp = 3
    private var playerHp = 3
    private var totalTime = 60_000L
    private var interval = 100L
    private lateinit var timer: CountDownTimer
    private var currentProblem: UTFConversionProblem? = null
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
        currentProblem = generateRandomUTFConversionProblem()
        val problem = currentProblem!!
        Log.d("UTF_CONVERSION", "Correct answer: ${problem.correctAnswer}")
        viewBinding.frombaseTv.setText(problem.fromBaseName.capitalized()+":")
        viewBinding.frombasenumTv.setText(problem.displayValue.uppercase())
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