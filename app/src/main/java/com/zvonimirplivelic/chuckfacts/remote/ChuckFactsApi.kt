package com.zvonimirplivelic.chuckfacts.remote

import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.model.ChuckFactList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckFactsApi {

    @GET("random")
    suspend fun getRandomFact(): Response<ChuckFact>

    @GET("search")
    suspend fun searchForFact(
        @Query("query") searchString: String
    ): Response<ChuckFactList>

}