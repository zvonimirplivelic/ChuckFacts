package com.zvonimirplivelic.chuckfacts.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zvonimirplivelic.chuckfacts.model.ChuckFact

@Dao
interface ChuckFactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertFact(chuckFact: ChuckFact)

    @Delete
    suspend fun deleteFact(chuckFact: ChuckFact)

    @Query("SELECT * FROM chuck_facts")
    fun getAllFacts(): LiveData<List<ChuckFact>>
}
