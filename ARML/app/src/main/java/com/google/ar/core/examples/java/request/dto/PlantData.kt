package com.google.ar.core.examples.java.request.dto

import com.google.gson.annotations.SerializedName

data class PlantData(@SerializedName("plant_name") val plantName: String?,
                     @SerializedName("plant_leaf_state") val plantLeafState: String?,
                     @SerializedName("plant_solution") val plantSolution: String?,
                     @SerializedName("plant_temp") val plantTemp: String?,
                     @SerializedName("plant_life") val plantLife: String?,
                     @SerializedName("plant_water") val plantWater: String?,
                     @SerializedName("plant_fertile") val plantFertile: String?,
)

