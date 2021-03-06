package com.zvonimirplivelic.chuckfacts.repository

import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.remote.RetrofitInstance

class ChuckFactsRepository(private val db: ChuckFactsDatabase) {

    suspend fun getRandomChuckFact() =
        RetrofitInstance.api.getRandomFact()

    suspend fun searchForFact(searchString: String) =
        RetrofitInstance.api.searchForFact(searchString)


    fun getSavedFacts() = db.getFactsDao().getAllFacts()

    suspend fun saveChuckFact(chuckFact: ChuckFact) =
        db.getFactsDao().updateOrInsertFact(chuckFact)

    suspend fun deleteChuckFact(chuckFact: ChuckFact) =
        db.getFactsDao().deleteFact(chuckFact)

    suspend fun clearFactTable() = db.getFactsDao().deleteAllFacts()
}