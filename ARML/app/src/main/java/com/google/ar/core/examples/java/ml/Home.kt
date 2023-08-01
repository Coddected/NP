package com.google.ar.core.examples.java.ml

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Home : AppCompatActivity() {

    private val shapeDrawableResources = arrayOf(
        R.drawable.shape_drawable_1,
        R.drawable.shape_drawable_2,
        R.drawable.shape_drawable_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val addButton = findViewById<ImageButton>(R.id.addButton)
        addButton.setOnClickListener {
            addRandomImageButton()
        }

        val id = intent.getStringExtra("ID")
        val greetingTextView = findViewById<TextView>(R.id.greet)
        greetingTextView.text = "$id"
        val myplantTextView = findViewById<TextView>(R.id.myplant)
        myplantTextView.text = "       $id 의 화분"

        val buttonClick = findViewById<ImageButton>(R.id.ScanButton)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addRandomImageButton() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("식물의 이름을 입력해주세요")

        val alertDialog = dialogBuilder.create()

        // 커스텀 다이얼로그 내 "추가" 버튼 클릭 이벤트 처리
        val buttonAddItem = dialogView.findViewById<ImageButton>(R.id.buttonAddItem)
        buttonAddItem.setOnClickListener {
            // EditText에서 입력한 이름 가져오기
            val editTextItemName = dialogView.findViewById<TextView>(R.id.editTextItemName)
            val itemName = editTextItemName.text.toString().trim()

            if (itemName.isNotEmpty()) {
                val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(16, 16, 16, 16)

                val randomShapeDrawable = getRandomShapeDrawable()
                val imageButton = ImageButton(this)
                imageButton.background = null // 배경을 null로 설정하여 투명하게 만듭니다
                imageButton.setImageDrawable(randomShapeDrawable)
                imageButton.layoutParams = layoutParams
                containerLayout.addView(imageButton)

                // ImageButton 아래에 이름을 표시하는 TextView 추가
                val nameTextView = TextView(this)
                nameTextView.text = itemName
                nameTextView.layoutParams = layoutParams
                containerLayout.addView(nameTextView)

                // 다이얼로그 닫기
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }



    private fun getRandomShapeDrawable(): Drawable {
        val randomIndex = Random.nextInt(shapeDrawableResources.size)
        return resources.getDrawable(shapeDrawableResources[randomIndex], theme)
    }

}
