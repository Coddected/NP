package com.google.ar.core.examples.java.ml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.Button

class NewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val buttonClick = findViewById<Button>(R.id.start_button)
    buttonClick.setOnClickListener {
      val intent = Intent(this, Home::class.java)
      startActivity(intent)
    }
  }
}