package com.zvonimirplivelic.chuckfacts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zvonimirplivelic.chuckfacts.model.ChuckFact

@Database(
    entities = [ChuckFact::class],
    version = 1,
    exportSchema = false
)
abstract class ChuckFactsDatabase : RoomDatabase() {

    abstract fun getFactsDao(): ChuckFactsDao

    companion object {
        @Volatile
        private var INSTANCE: ChuckFactsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ChuckFactsDatabase::class.java,
                "chuck_facts_db"
            ).build()
    }
}