package com.google.ar.core.examples.java.ml

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.examples.java.request.ServiceBuilder
import com.google.ar.core.examples.java.request.dto.NestedPlantData
import com.google.ar.core.examples.java.request.dto.PlantData
import retrofit2.Call
import retrofit2.Response


class Plant_info : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plant_info)
        val serviceBuilder = ServiceBuilder.myApi
        var plantData: PlantData? = null
        var builderBoolean: Boolean? = null
        val context: Context = this


        serviceBuilder.plantInfo()
            .enqueue(object : retrofit2.Callback<NestedPlantData> {
                override fun onResponse(
                    call: Call<NestedPlantData>,
                    response: Response<NestedPlantData>
                ) {
                    val responseCode = response.code()
                    if (responseCode == 200) {
                        Toast.makeText(context, "Save Completed !", Toast.LENGTH_SHORT).show()

                        val plantName = response.body()!!.data!!.plantName
                        val plantLeafState = response.body()!!.data!!.plantName
                        val plantSolution = response.body()!!.data!!.plantSolution
                        val plantTemp = response.body()!!.data!!.plantTemp
                        val plantLife = response.body()!!.data!!.plantLife
                        val plantWater = response.body()!!.data!!.plantWater
                        val plantFertile = response.body()!!.data!!.plantFertile
                        val temperTextView = findViewById<TextView>(R.id.temper)
                        val liveTextView = findViewById<TextView>(R.id.live)
                        val waterTextView = findViewById<TextView>(R.id.water)
                        val fertileTextView = findViewById<TextView>(R.id.fertile)
                        val plantExplain = findViewById<TextView>(R.id.explain )
                        Toast.makeText(context, plantLife, Toast.LENGTH_LONG).show()

                        plantData = PlantData(
                            plantName, plantLeafState, plantSolution, plantTemp, plantLife, plantWater,
                            plantFertile
                        )

                        builderBoolean = true

                        temperTextView.text = plantTemp
                        liveTextView.text = plantLife
                        waterTextView.text = plantWater
                        fertileTextView.text = plantFertile
                        plantExplain.text = plantSolution

                    }
                }

                override fun onFailure(call: Call<NestedPlantData>, t: Throwable) {
                    Toast.makeText(context, "Failed due ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        val count = intent.getIntExtra("Count", 0)

        val backButton = findViewById<ImageButton>(R.id.Back)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("Count", count)

            setResult(RESULT_OK, intent)
            finish()
        }

        val interrogationButton = findViewById<ImageButton>(R.id.Interrogation)
        interrogationButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}
