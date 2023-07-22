package com.google.ar.core.examples.java.request

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIService {
    @Multipart
    @POST("/upload_images")
    suspend fun uploadFile(@Part body: MultipartBody.Part)
}