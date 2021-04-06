package com.zvonimirplivelic.chuckfacts.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.zvonimirplivelic.chuckfacts.R
import com.zvonimirplivelic.chuckfacts.database.ChuckFactsDatabase
import com.zvonimirplivelic.chuckfacts.repository.ChuckFactsRepository
import com.zvonimirplivelic.chuckfacts.util.Constants
import com.zvonimirplivelic.chuckfacts.util.Constants.FACT_NOTIFICATION_CHANNEL_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PeriodicFactWork(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val chuckFactsRepository = ChuckFactsRepository(ChuckFactsDatabase.invoke(applicationContext))

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        try {

            val notification = NotificationCompat.Builder(applicationContext, FACT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_new_chuck_fact)
                .setContentTitle("New Chuck Fact")
                .setContentText(getNotificationString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            createNotificationChannel()
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1, notification)

                Timber.d("Notification text: ${getNotificationString()}")
            }
        } catch (e: Exception) {

        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
                FACT_NOTIFICATION_CHANNEL_ID,
        "New Chuck Fact",
        NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "This is your MainActivity's test channel"
        }

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun getNotificationString(): String {
        return withContext(Dispatchers.IO) {
            chuckFactsRepository.getRandomChuckFact().body()!!.value
        }
    }

}
