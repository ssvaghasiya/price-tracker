package com.example.transoapp.retrofit

import com.example.transoapp.pojo.ExampleData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {

    @POST("market-pairs")
    suspend fun getExamples(): Response<ExampleData?>


}