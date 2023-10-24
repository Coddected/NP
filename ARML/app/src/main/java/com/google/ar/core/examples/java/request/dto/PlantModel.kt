package com.google.ar.core.examples.java.request.dto

import com.google.gson.annotations.SerializedName

data class PlantModel(@SerializedName("plant_name") val plantName: String?,
                      @SerializedName("plant_leaf_state") val plantLeafState: String?,
)

