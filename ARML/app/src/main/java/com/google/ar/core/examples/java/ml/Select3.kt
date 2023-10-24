package com.google.ar.core.examples.java.ml

import android.content.Intent
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.examples.java.ml.Home
import com.google.ar.core.examples.java.ml.R

//import com.google.ar.core.examples.java.request.YourResponseModel
//import com.google.ar.core.examples.java.request.YourRequestModel

class Select3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select3)
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

        val button1 = findViewById<Button>(R.id.button9)
        val button2 = findViewById<Button>(R.id.button10)
        val button3 = findViewById<Button>(R.id.button11)
        val button4 = findViewById<Button>(R.id.button12)

        data class YourRequestModel(val text: String)

        button1.setOnClickListener {
            val buttonText = button1.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val buttonText = button2.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val buttonText = button3.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        button4.setOnClickListener {
            val buttonText = button4.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

    }

    private fun sendDataToServer(text: String) {
//        val serviceBuilder = ServiceBuilder.myApi
//
//        val requestData = YourRequestModel
//
//        val call = serviceBuilder.postDataToServer(requestData)
//
//        call.enqueue(object : Callback<YourResponseModel> {
//            override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
//                Toast.makeText(this@select3, "데이터 전송 중 오류 발생", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}
