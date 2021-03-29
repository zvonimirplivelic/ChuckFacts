package com.zvonimirplivelic.chuckfacts.repository

import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.remote.RetrofitInstance

class ChuckFactsRepository(val db: ChuckFactsDatabase) {

    suspend fun getRandomChuckFact() =
        RetrofitInstance.api.getRandomFact()

}