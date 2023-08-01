package com.google.ar.core.examples.java.request;

//import live.tek.form_data.QuestionsResponse
import com.google.ar.core.examples.java.request.Result
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IApi {

    @Multipart
    @POST("api/images/")
    fun uploadImage( @Part image:MultipartBody.Part):Call<Result>


}