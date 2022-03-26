package com.example.mytest.Interface

import retrofit2.http.GET
import com.example.mytest.Model.Videolist
import retrofit2.Call
import retrofit2.http.Query

interface Api_Interface {
    @GET("users")
    fun getList(@Query("page") page: Int): Call<Videolist?>?
}