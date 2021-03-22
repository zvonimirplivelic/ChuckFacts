package com.zvonimirplivelic.chuckfacts.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.chucknorris.io/jokes/"

object RetrofitInstance {

    val api: ChuckFactsApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChuckFactsApi::class.java)
    }
}