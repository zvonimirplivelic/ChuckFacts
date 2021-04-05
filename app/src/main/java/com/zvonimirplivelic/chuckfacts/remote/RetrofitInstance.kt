package com.zvonimirplivelic.chuckfacts.remote

import com.zvonimirplivelic.chuckfacts.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val api: ChuckFactsApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChuckFactsApi::class.java)
    }
}