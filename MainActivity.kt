package com.glitchlab.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(15, 15, 25)
        window.navigationBarColor = Color.rgb(15, 15, 25)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(24), dp(24), dp(24), dp(24)); setBackgroundColor(Color.rgb(15,15,25)) }
        val title = TextView(this).apply { text = "GLITCH LAB"; textSize = 30f; setTextColor(Color.WHITE); gravity = Gravity.CENTER; typeface = android.graphics.Typeface.DEFAULT_BOLD }
        val desc = TextView(this).apply { text = "Random visual sessions • Android overlay"; textSize = 15f; setTextColor(Color.LTGRAY); gravity = Gravity.CENTER; setPadding(0, dp(8), 0, dp(24)) }
        root.addView(title, LinearLayout.LayoutParams(-1, -2)); root.addView(desc, LinearLayout.LayoutParams(-1, -2))
        // Keep user settings across app closes and phone restarts.
        val prefs = getSharedPreferences("glitch_lab_settings", MODE_PRIVATE)
        val savedMin = prefs.getInt("minWait", 15).coerceIn(15, 300)
        val savedMax = prefs.getInt("maxWait", 60).coerceIn(savedMin, 555)
        val minLabel = TextView(this).apply { text = "Minimum wait (seconds): $savedMin"; setTextColor(Color.WHITE) }
        val minSeek = SeekBar(this).apply { max = 285; progress = savedMin - 15 }
        val maxLabel = TextView(this).apply { text = "Maximum wait (seconds): $savedMax"; setTextColor(Color.WHITE) }
        val maxSeek = SeekBar(this).apply { max = 540; progress = savedMax - 15 }
        minSeek.setOnSeekBarChangeListener(listener {
            minLabel.text = "Minimum wait (seconds): ${it + 15}"
            if (maxSeek.progress < it) { maxSeek.progress = it; maxLabel.text = "Maximum wait (seconds): ${it + 15}" }
            prefs.edit().putInt("minWait", it + 15).putInt("maxWait", maxSeek.progress + 15).apply()
        })
        maxSeek.setOnSeekBarChangeListener(listener {
            val safeProgress = maxOf(it, minSeek.progress)
            if (safeProgress != it) maxSeek.progress = safeProgress
            maxLabel.text = "Maximum wait (seconds): ${safeProgress + 15}"
            prefs.edit().putInt("minWait", minSeek.progress + 15).putInt("maxWait", safeProgress + 15).apply()
        })
        root.addView(minLabel); root.addView(minSeek); root.addView(maxLabel); root.addView(maxSeek)
        val info = TextView(this).apply { text = "A session can cover other apps only after you grant overlay permission. The effect has a visible STOP button and stops when the screen turns off. Start the session here; it will not secretly start after reboot."; setTextColor(Color.LTGRAY); textSize = 14f; setPadding(0, dp(18), 0, dp(18)) }
        root.addView(info)
        val permission = Button(this).apply { text = "1. Allow display over other apps"; setOnClickListener { startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) } }
        val start = Button(this).apply { text = "2. Start random sessions"; setOnClickListener {
            if (!Settings.canDrawOverlays(this@MainActivity)) { Toast.makeText(this@MainActivity, "First allow display over other apps", Toast.LENGTH_LONG).show(); return@setOnClickListener }
            val min = minSeek.progress + 15
            val max = maxSeek.progress + 15
            val i = Intent(this@MainActivity, GlitchService::class.java).putExtra("minWait", min).putExtra("maxWait", max)
            startService(i)
            Toast.makeText(this@MainActivity, "Session scheduler started. Use the notification or overlay STOP button to stop.", Toast.LENGTH_LONG).show()
        } }
        val stop = Button(this).apply { text = "Stop sessions"; setOnClickListener { stopService(Intent(this@MainActivity, GlitchService::class.java)); Toast.makeText(this@MainActivity, "Stopped", Toast.LENGTH_SHORT).show() } }
        root.addView(permission, LinearLayout.LayoutParams(-1, -2)); root.addView(start, LinearLayout.LayoutParams(-1, -2)); root.addView(stop, LinearLayout.LayoutParams(-1, -2))
        setContentView(ScrollView(this).apply { addView(root) })
    }
    private fun listener(update: (Int) -> Unit) = object : android.widget.SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) { update(progress) }
        override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
    }
}
