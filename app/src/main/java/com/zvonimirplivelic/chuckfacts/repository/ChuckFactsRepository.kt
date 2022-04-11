package com.zvonimirplivelic.chuckfacts.repository

import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDao
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.model.ChuckFact
import com.zvonimirplivelic.chuckfacts.remote.RetrofitInstance

class ChuckFactsRepository(private val chuckDao: ChuckFactsDao) {

    suspend fun getRandomChuckFact() =
        RetrofitInstance.api.getRandomFact()

    suspend fun searchForFact(searchString: String) =
        RetrofitInstance.api.searchForFact(searchString)

    fun getSavedFacts() = chuckDao.getAllFacts()

    suspend fun saveChuckFact(chuckFact: ChuckFact) =
        chuckDao.updateOrInsertFact(chuckFact)

    suspend fun deleteChuckFact(chuckFact: ChuckFact) =
        chuckDao.deleteFact(chuckFact)

    suspend fun clearFactTable() = chuckDao.deleteAllFacts()
}