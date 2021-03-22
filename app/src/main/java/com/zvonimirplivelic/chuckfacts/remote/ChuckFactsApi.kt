package com.zvonimirplivelic.chuckfacts.remote

import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import retrofit2.Response
import retrofit2.http.GET

interface ChuckFactsApi {

    @GET("random")
    suspend fun getRandomFact(): Response<ChuckFact>
}