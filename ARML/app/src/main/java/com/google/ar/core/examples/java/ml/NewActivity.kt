package com.google.ar.core.examples.java.ml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.ImageView

class NewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val gifImageView = findViewById<ImageView>(R.id.gifImageView)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      // Android 9.0 (Pie) 이상
      val animatedImageDrawable = gifImageView.drawable as AnimatedImageDrawable
      animatedImageDrawable.start()
    } else {
      // Android 9.0 미만
      val animationDrawable = gifImageView.drawable as AnimationDrawable
      animationDrawable.start()
    }

    val buttonClick = findViewById<Button>(R.id.start_button)
    buttonClick.setOnClickListener {
      val intent = Intent(this, Home::class.java)
      startActivity(intent)
    }
  }
}