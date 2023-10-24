package com.google.ar.core.examples.java.request;

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class ServiceBuilder {
    companion object {

        val myApi: IApi by lazy {
            return@lazy getRetrofit().create(IApi::class.java)
        }
        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getRetrofit(): Retrofit {

            val temp = INSTANCE
            val url: String = "http://221.159.102.58:8000/"

            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}