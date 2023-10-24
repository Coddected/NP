package com.google.ar.core.examples.java.request.dto

import com.google.gson.annotations.SerializedName

data class NestedPlantData(@SerializedName("data") var data: PlantData?)