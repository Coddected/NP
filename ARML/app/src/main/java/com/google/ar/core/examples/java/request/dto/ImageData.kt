package com.google.ar.core.examples.java.request.dto

import com.google.gson.annotations.SerializedName

data class ImageData(@SerializedName("image_url") val imageUrl: String?)
