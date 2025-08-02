package com.mobicom.s16.csarchers


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.mobicom.s16.csarchers.databinding.ActivityTopicsBinding
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.mobicom.s16.csarchers.leaderboard.LeaderboardActivity

class TopicsActivity : ComponentActivity() {
    private fun showPopup(anchorView: View, message: String) {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_description, null)
        val textView = popupView.findViewById<TextView>(R.id.desc_text)
        textView.text = message

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
            isFocusable = true // Needed so it can dismiss when clicking outside
            elevation = 10f
        }

        // Show popup just *below* the anchor view
        anchorView.post {
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            // Offset below the button
            val yOffset = anchorView.height // Add some margin
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x + 25, y + yOffset)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityTopicsBinding = ActivityTopicsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val intent = getIntent()
        val mode = intent.getStringExtra("Mode_Key")

        viewBinding.selectModeBtnLeaderboard.setOnClickListener({
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        })

        if(mode == "Game"){
            // arithmetic
            viewBinding.intArithmeticBtn.setOnClickListener {
                val intent = Intent(this, GameActivityIntArithmetic::class.java)
                startActivity(intent)
            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {
                val intent = Intent(this, GameActivityNumSys::class.java)
                startActivity(intent)
            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {
                val intent = Intent(this, GameActivityIEEE::class.java)
                startActivity(intent)
            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {
                val intent = Intent(this, GameActivityUTF::class.java)
                startActivity(intent)
            }
        }

        if(mode == "Simulator"){
            // arithmetic
            viewBinding.intArithmeticBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "intarith")
                startActivity(intent)
            }

            // conversion
            viewBinding.numSysBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "numsys")
                startActivity(intent)
            }

            // IEEE
            viewBinding.ieeeBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "ieee")
                startActivity(intent)
            }

            // UTF
            viewBinding.utfBtn.setOnClickListener {
                val intent = Intent(this, TopicsSimActivity::class.java)
                intent.putExtra("SIM_MODE_KEY", "utf")
                startActivity(intent)
            }
        }

        // Show tooltips on long press
        viewBinding.intArithmeticBtn.setOnLongClickListener {
            showPopup(it, "Perform basic operations using whole numbers, such as addition, subtraction, multiplication, and division.\n\n" +
                    "Understand how integers behave during calculations and how overflow or signed/unsigned values can affect the result.")
            true
        }

        viewBinding.numSysBtn.setOnLongClickListener {
            showPopup(it, "Convert numbers between different bases like binary (base-2), octal (base-8), decimal (base-10), and hexadecimal (base-16).\n\n" +
                    "Learn how computers represent and manipulate numbers using various base systems.")
            true
        }

        viewBinding.ieeeBtn.setOnLongClickListener {
            showPopup(it, "Represent decimal numbers in binary using the IEEE 754 floating-point standard.\n\n" +
                    "Explore how computers store real numbers using formats like single-precision (32-bit) and double-precision (64-bit), including sign, exponent, and mantissa.")
            true
        }

        viewBinding.utfBtn.setOnLongClickListener {
            showPopup(it, "Understand how characters and symbols are encoded as numbers using Unicode and formats like UTF-8, UTF-16, and UTF-32.\n\n" +
                    "Learn how different languages, emojis, and special characters are stored and interpreted by computers using standardized encoding.")
            true
        }


        val mydbHelper = MyDbHelper(this)
        viewBinding.topicsLogoutBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    mydbHelper.logout()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
