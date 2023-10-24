package com.google.ar.core.examples.java.request

import com.google.ar.core.examples.java.request.dto.ImageData
import com.google.ar.core.examples.java.request.dto.NestedPlantData
import com.google.ar.core.examples.java.request.dto.PlantData
import com.google.ar.core.examples.java.request.dto.PlantModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IApi {

    @Multipart
    @POST("api/images/save")
    fun uploadImage(@Part image: MultipartBody.Part): Call<Result>

//    @POST("/api/data/place")
//    fun postDataToServer(@Body requestData: YourRequestModel): Call<Result>

    @POST("/api/images/classify")
    fun classifyImage(@Body imageData: ImageData?): Call<Result>

    @POST("/api/plant/info/save")
    fun savePlantInfo(@Body plantModel: PlantModel?): Call<ResultId>

    @GET("/api/plant/info")
    fun plantInfo(): Call<NestedPlantData>

    @GET("/api/plant/info/list")
    fun getPlantInfoList(): Call<Result>
}
