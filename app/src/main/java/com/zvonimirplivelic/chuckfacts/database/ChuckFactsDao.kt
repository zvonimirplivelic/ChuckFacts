package com.zvonimirplivelic.chuckfacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zvonimirplivelic.chuckfacts.model.ChuckFact

@Dao
interface ChuckFactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertFact(chuckFact: ChuckFact)

    @Query("DELETE FROM chuck_facts WHERE id = :factId")
    suspend fun deleteFact(factId: String)


    @Query("DELETE FROM chuck_facts")
    suspend fun deleteAllFacts()

    @Query("SELECT * FROM chuck_facts")
    fun getAllFacts(): LiveData<List<ChuckFact>>
}
