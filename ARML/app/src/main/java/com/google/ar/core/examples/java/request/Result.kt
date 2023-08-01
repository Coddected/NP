package com.google.ar.core.examples.java.request;


data class Result(
    val message:String,
    val data:List<DataItem>
)


data class DataItem(val url:String, val id:String)