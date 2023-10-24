
package com.google.ar.core.examples.java.ml

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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
        var count = 0

//        val addButton = findViewById<ImageButton>(R.id.addButton)
//        addButton.setOnClickListener {
//            addRandomImageButton()
//            count ++
//        }


//        val id = intent.getStringExtra("ID")
        val id = "Guest110"
        val greetingTextView = findViewById<TextView>(R.id.greet)
        greetingTextView.text = "$id"
        val myplantTextView = findViewById<TextView>(R.id.myplant)
        myplantTextView.text = "       $id 의 화분"

        val buttonClick = findViewById<ImageButton>(R.id.ScanButton)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val backbuttonClick = findViewById<ImageButton>(R.id.Back)
        backbuttonClick.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        val IrbuttonClick = findViewById<ImageButton>(R.id.Interrogation)
        IrbuttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val PlantbuttonClick = findViewById<ImageButton>(R.id.Plant)
        PlantbuttonClick.setOnClickListener {
            val intent = Intent(this, Plants::class.java)
            intent.putExtra("Count", count)
            startActivity(intent)
        }
    }


    private fun addRandomImageButton() {
        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(16, 16, 16, 16)

        val randomShapeDrawable = getRandomShapeDrawable()
        val imageButton = ImageButton(this)
        imageButton.background = null // Set the background to null to make it transparent
        imageButton.setImageDrawable(randomShapeDrawable)
        imageButton.layoutParams = layoutParams
        containerLayout.addView(imageButton)
    }


    private fun getRandomShapeDrawable(): Drawable {
        val randomIndex = Random.nextInt(shapeDrawableResources.size)
        return resources.getDrawable(shapeDrawableResources[randomIndex], theme)
    }
}