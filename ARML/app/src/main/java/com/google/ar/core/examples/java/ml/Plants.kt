package com.google.ar.core.examples.java.ml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout

class Plants : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plants)

        val count = intent.getIntExtra("Count", 0)
        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)

        val buttonHeight = resources.getDimensionPixelSize(R.dimen.button_height) // 버튼 높이 리소스로 대체
        val buttonMarginBottom = resources.getDimensionPixelSize(R.dimen.button_margin_bottom) // 버튼 간격 리소스로 대체

        // 텍스트 버튼만 추가하기 위한 반복문
        for (i in 1..count) {
            val textButton = Button(this)
            // 텍스트 버튼의 속성을 설정합니다.
            textButton.text = "버튼 $i" // 원하는 버튼 텍스트로 대체
            textButton.setBackgroundResource(R.drawable.plantbutton) // 이미지 버튼의 배경으로 설정
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, buttonHeight)
            layoutParams.setMargins(0, 0, 0, buttonMarginBottom) // 간격 설정
            textButton.layoutParams = layoutParams
            containerLayout.addView(textButton)

            // 텍스트 버튼 클릭 이벤트 처리
            textButton.setOnClickListener {
                val num = i
                val intent = Intent(this, Plant_info::class.java)
                intent.putExtra("ButtonNumber", num)
                startActivity(intent)
            }
        }

        // 뒤로 가기 버튼 클릭 이벤트 처리
        val backButton = findViewById<ImageButton>(R.id.Back)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("Count", count)

            setResult(RESULT_OK, intent)
            finish()
        }

        // 물음표 버튼 클릭 이벤트 처리
        val interrogationButton = findViewById<ImageButton>(R.id.Interrogation)
        interrogationButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}